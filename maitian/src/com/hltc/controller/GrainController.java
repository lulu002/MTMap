package com.hltc.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IGrainDao;
import com.hltc.entity.Comment;
import com.hltc.entity.Grain;
import com.hltc.service.IGrainService;
import com.hltc.service.IUserService;
import static com.hltc.util.SecurityUtil.*;


/**
 * 麦粒模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/grain")
public class GrainController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IGrainService grainService;
	@Autowired
	private IGrainDao grainDao;
	@Autowired
	private ICommentDao commentDao;
	
	@RequestMapping(value="/publish_batch.json", method = RequestMethod.POST)
	public @ResponseBody Object publishByFile(HttpServletRequest request, @RequestParam(value="cityCode") String cityCode){
		System.out.println(cityCode);
//		Integer minLine =  Integer.valueOf(request.getParameter("minLine")) -1;
//		Integer maxLine =  (Integer) request.getAttribute("maxLine") -1;
//		String cityCode = (String) request.getAttribute("cityCode");
//		System.out.println(minLine);
//		System.out.println(maxLine);
//		System.out.println(cityCode);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 使用系统临时路径
		String path = System.getProperty("user.home");
		factory.setRepository(new File(path));
		factory.setSizeThreshold(1024 * 1024);

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		
		try {
			//这个方法支持多文件上传，本例子中不需要
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单的属性名字
				String name = item.getFieldName();
				
				// 如果获取的表单信息是普通的文本信息，上传有时候是混在表单中的，本例中不需要考虑
				if (item.isFormField()) {
					String value = new String((item.getString("iso8859-1")).getBytes("iso8859-1"),"utf-8");
					System.out.println(name+":"+value);
				} else {
//					获取上传路径
//					String value = item.getName();
//					int start = value.lastIndexOf("\\");
//					获得上传文件名
//					String filename = value.substring(start + 1);
//					将文件写到磁盘，如果不解析就成了文件上传功能了
//					item.write(new File(path, filename));
					//-----------进行解析---------
					Workbook wb = new HSSFWorkbook(item.getInputStream());
					Sheet sheet1 = wb.getSheetAt(0);
//					sheet1.setColumnHidden(*,true); //隐藏列
					
					for (Row row : sheet1) {
						Integer num = row.getRowNum();
//						if(num < minLine || num > maxLine) break;
						
				        for (Cell cell : row) {
//				        	excel单元格的索引
//				            CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
//				            System.out.print("索引"+cellRef.formatAsString());
				            
				            switch (cell.getCellType()) {
//				            	文本内容
				                case Cell.CELL_TYPE_STRING:
				                    System.out.println(cell.getRichStringCellValue().getString());
				                    break;
//								数字与日期
				                case Cell.CELL_TYPE_NUMERIC:
				                    if (DateUtil.isCellDateFormatted(cell)) {
				                        System.out.println(cell.getDateCellValue());
				                    } else {
				                        System.out.println(cell.getNumericCellValue());
				                    }
				                    break;
//				                                                      公式数据
				                case Cell.CELL_TYPE_FORMULA:
				                    System.out.println(cell.getCellFormula());
				                    break;
				                default:
				                    System.out.println();
				            }
				        }
				    }
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------out-------");
		return Result.success();
	}
	
	/**
	 * 发布麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/publish.json", method=RequestMethod.POST)
	public @ResponseBody Object publish(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","token","mcateId","siteName","siteAddress","lon","lat","cityCode","isPublic","text"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"siteId","sitePhone", "siteType"}, false, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"images"}, false, JSONArray.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersScope(jobj, "siteSource", true, new String[]{"0","1"});
		if(null == result.get(Result.SUCCESS))	return result;
				
		//step1 登录验证
		String userId = jobj.getString("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		
		if(null == result.get("success")){
			return result;
		}
		return  grainService.publish(jobj);
	}
	
	/**
	 * 首页查询朋友的麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/home_query.json", method=RequestMethod.POST)
	public @ResponseBody Object homeQuery(@RequestBody JSONObject jobj){
		//step0 参数检验
		HashMap<String, Class> map = new HashMap<String, Class>();
		Map result = null;
		map.put("user_id", String.class);
		map.put("token", String.class);
		map.put("mcate_id", String.class);
		map.put("city_code", String.class);
		map.put("lon", String.class);
		map.put("lat", String.class);
		result = parametersValidate(jobj, map);
		if(null == result.get("success")){
			return result;
		}
		
		//step1 登录验证
		String userId = jobj.getString("user_id");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 查询麦粒
		Double lon = null, lat = null, radius = null;
		try {
			lon = Double.valueOf(jobj.getString("lon"));
			lat = Double.valueOf(jobj.getString("lat"));
			Object r = jobj.get("radius");
			radius = null == r ? null: Double.valueOf(r.toString());
		} catch (Exception e) {
			return Result.fail(ErrorCode.PARAMS_ERROR, "parameters eorr: lon,lat and radius should be double string");
			//TODO log exception
		}
	
		List<Grain> grains = grainDao.findFriendsGrain(userId, jobj.getString("mcate_id"), jobj.getString("city_code"), lon, lat, radius);
		
		return Result.success(grains);
	}
	
	/**
	 * 点赞
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/praise.json", method=RequestMethod.POST)
	public @ResponseBody Object praise(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 点赞表创建或者删除记录
		return grainService.praise(jobj.getString("gid"), userId);
	}
	
	/**
	 * 忽略麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/ignore.json", method=RequestMethod.POST)
	public @ResponseBody Object ignore(@RequestBody JSONObject jobj){
		//step0 参数验证
		
		
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 创建忽略记录
		return grainService.ignore(jobj.getString("gid"), userId);
		
	}
	
	/**
	 * 收藏麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/favor.json", method=RequestMethod.POST)
	public @ResponseBody Object favor(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 创建收藏记录
		return grainService.favor(jobj.getString("gid"), userId);
	}
	
	/**
	 * 评论麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/comment.json", method=RequestMethod.POST)
	public @ResponseBody Object comment(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 麦粒是否存在
		Grain grain = grainDao.findById(jobj.getString("gid"));
		if(null == grain){
			return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		}
		
		return grainService.createComment(jobj);
	}
	
	/**
	 * 删除评论
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/del_comment.json", method=RequestMethod.POST)
	public @ResponseBody Object delComment(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, (String)jobj.get("token"));
		if(null == result.get("success")){
			return result;
		}
	
		//step2 删除评论
		Comment comment = commentDao.findById((String)jobj.get("cid"));
		if(null == comment){
			return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
		}
		
		if(!userId.equals(comment.getUserId())){
			return Result.fail(ErrorCode.COMMENT_DEL_FAILED);
		}
		
		commentDao.delete(comment);
		
		return Result.success();
	}
	
	/**
	 *  麦粒详情
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/detail.json", method=RequestMethod.POST)
	public @ResponseBody Object getDetail(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//TODO
		return null;
	}
	
	/**
	 *  删除麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/delete.json", method=RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 delete
		return grainService.deleteGrain(jobj.getString("gid"));
	}
	public static void main(String[] args) {
		JSONObject o = new JSONObject();
		o.put("23","234.33");
		
		Object ob = o.get("23");
		
		
		System.out.println(Double.valueOf(ob.toString()));
	}
}
