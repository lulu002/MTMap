package com.hltc.controller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hltc.common.ErrorCode;
import com.hltc.common.Pager;
import com.hltc.common.Result;
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.IRecommendDao;
import com.hltc.dao.ITokenDao;
import com.hltc.entity.Comment;
import com.hltc.entity.Grain;
import com.hltc.entity.Recommend;
import com.hltc.entity.Token;
import com.hltc.entity.Vrecommend;
import com.hltc.service.IGrainService;
import com.hltc.service.IUserService;
import com.hltc.util.OSSUtil;
import com.hltc.util.UUIDUtil;

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
	@Autowired
	private ITokenDao tokenDao;
	@Autowired
	private IRecommendDao recommendDao;
	
	@RequestMapping(value="/publish_batch.json/{minLine}/{maxLine}/{cityCode}", method = RequestMethod.POST)
	public @ResponseBody Object publishByFile(HttpServletRequest request, @PathVariable Integer minLine, @PathVariable Integer maxLine, @PathVariable String cityCode){
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
					String[] columnArr = new String[]{"","siteId","siteName","siteAddress","sitePhone","siteType","lon","lat","mcateId","text"};
					for (Row row : sheet1) {
						Integer num = row.getRowNum();
						if(num < minLine -1) continue;
						if(num > maxLine -1) break;
						Integer i = 0;
						
						JSONObject jobj = new JSONObject();
				        for (Cell cell : row) {
				          if(++i > 9) break;
				          
				          if(i == 8){  //类别
				        	  jobj.put(columnArr[i], Pattern.compile("[^0-9]").matcher(cell.getRichStringCellValue().getString()).replaceAll(""));
				        	  continue;
				          }
				         
//				        	excel单元格的索引
//				            CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
//				            System.out.print("索引"+cellRef.formatAsString());
				            
				            switch (cell.getCellType()) {
//				            	文本内容
				                case Cell.CELL_TYPE_STRING:
				                    jobj.put(columnArr[i], cell.getRichStringCellValue().getString());
				                    break;
//								数字与日期
				                case Cell.CELL_TYPE_NUMERIC:
				                    if (DateUtil.isCellDateFormatted(cell)) {
				                    	jobj.put(columnArr[i], cell.getDateCellValue());
				                    } else {
				                    	 if(i == 4){
				                    		 DecimalFormat df = new DecimalFormat("#");
				                    		 jobj.put(columnArr[i], df.format(cell.getNumericCellValue()));
				                    	 }else{
				                    		 jobj.put(columnArr[i], cell.getNumericCellValue()+"");
				                    	 }
				                    }
				                    break;
//				                                                      公式数据
				                case Cell.CELL_TYPE_FORMULA:
				                	jobj.put(columnArr[i], cell.getCellFormula());
				                    break;
				                default:
				                    System.out.println();
				            }
				        }
				        jobj.put("cityCode", cityCode);
				        Token token = tokenDao.findATestToken();
				        jobj.put("userId",  token.getUserId());
				        jobj.put("token", token.getToken());
				        jobj.put("siteSource", "1");
				        jobj.put("isPublic", Math.random() > 0.5 ? "1" : "0");
				        grainService.publish(jobj);
				    }
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Map result =  parametersValidate(jobj, "userId",true,new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","mcateId","siteName","siteAddress","lon","lat","cityCode","isPublic","text"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"siteId","sitePhone", "siteType"}, false, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"images"}, false, JSONArray.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersScope(jobj, "siteSource", true, new String[]{"0","1"});
		if(null == result.get(Result.SUCCESS))	return result;
				
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")) return result;
		return  grainService.publish(jobj);
	}
	
	@RequestMapping(value="/getCategories.json", method=RequestMethod.GET)
	public @ResponseBody Object getCategories(@RequestBody JSONObject jobj){
		
		return null;
	}
	
	/**
	 * 通过查询条件获取麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/query_grain_list.json", method=RequestMethod.POST)
	public @ResponseBody Object queryByCondition(@RequestBody JSONObject jobj){
		Map result = parametersValidate(jobj, new String[]{"currentPage"}, true, Integer.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		Map map = new HashMap();
		
		Pager pager = new Pager(grainDao.countByCondition(jobj));
		pager.refresh(jobj.getInt("currentPage"));
		
		jobj.put("startRow", pager.getStartRow());
		
		List list = grainDao.queryByCondition(jobj);
		
		map.put("grain", list);
		map.put("pager", pager);
		return Result.success(map);
	}
	
	/**
	 * 首页查询朋友的麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/home_query.json", method=RequestMethod.POST)
	public @ResponseBody Object homeQuery(@RequestBody JSONObject jobj){
		//step0 参数检验
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS)) return result;
		result = parametersValidate(jobj, new String[]{"token","mcateId","cityCode","lon","lat"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		result = parametersValidate(jobj, new String[]{"radius"}, false, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step2 查询麦粒
		Double lon = null, lat = null, radius = null;
		try {
			lon = Double.valueOf(jobj.getString("lon"));
			lat = Double.valueOf(jobj.getString("lat"));
			Object r = jobj.get("radius");
			radius = null == r ? null: Double.valueOf(r.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.fail(ErrorCode.PARAMS_ERROR, "parameters eorr: lon,lat and radius should be double string");
		}
	
		List<Grain> grains = grainDao.findFriendsGrain(userId, jobj.getString("mcateId"), jobj.getString("cityCode"), lon, lat, radius);
		return Result.success(grains);
	}
	
	/**
	 * 点赞
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/praise.json", method=RequestMethod.POST)
	public @ResponseBody Object praise(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 点赞表创建或者删除记录
		return grainService.praise(jobj.getLong("gid"), userId);
	}
	
	/**
	 * 忽略麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/neglect.json", method=RequestMethod.POST)
	public @ResponseBody Object ignore(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 创建忽略记录
		return grainService.ignore(jobj.getLong("gid"), userId);
	}
	
	/**
	 * 收藏麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/favor.json", method=RequestMethod.POST)
	public @ResponseBody Object favor(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 创建收藏记录
		return grainService.favor(jobj.getLong("gid"), userId);
	}
	
	/**
	 * 评论麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/comment.json", method=RequestMethod.POST)
	public @ResponseBody Object comment(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","text"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, "tocid", false,new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 麦粒是否存在
		Grain grain = grainDao.findById(jobj.getLong("gid"));
		if(null == grain) return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		try {
			result = grainService.createComment(jobj);
		} catch (Exception e) {
			result = Result.fail(ErrorCode.DB_ERROR);
			e.printStackTrace();
		}
		return  result;
	}
	
	/**
	 * 删除评论
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/del_comment.json", method=RequestMethod.POST)
	public @ResponseBody Object delComment(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","cid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;

		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, (String)jobj.get("token"));
		if(null == result.get(Result.SUCCESS))	return result;
	
		//step2 删除评论
		Comment comment = commentDao.findById(jobj.getLong("cid"));
		if(null == comment){
			return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
		}
		
		if(!userId.equals(comment.getUserId()) || comment.getIsDeleted()){
			return Result.fail(ErrorCode.COMMENT_DEL_FAILED);
		}
		HashMap setParams = new HashMap();
		HashMap whereParams = new HashMap();
		setParams.put("is_deleted", true);
		whereParams.put("cid", comment.getCid());
		int exeResult = commentDao.updateByShard(setParams, "comment", "gid", comment.getGid(), whereParams);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	/**
	 *  麦粒详情
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/detail.json", method=RequestMethod.POST)
	public @ResponseBody Object getDetail(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step3
		try {
			result =  grainService.getGrainDetail(jobj.getLong("gid"), userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *  删除麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/delete.json", method=RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","gid"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 查询麦粒
		Long gid = jobj.getLong("gid");
		Grain grain = grainDao.findById(gid);
		if(null == grain){
			return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		}
		if(!userId.equals(grain.getUserId()) || grain.getIsDeleted()){
			return Result.fail(ErrorCode.GRAIN_DEL_FAILED);
		}
		HashMap setParams = new HashMap();
		setParams.put("is_deleted", true);
		int exeResult = grainDao.updateByShard(setParams, "grain", "gid", gid, null);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	
	/**
	 * 修改麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/update.json", method = RequestMethod.POST)
	public @ResponseBody Object update(HttpServletRequest request){
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 使用系统临时路径
		String path = System.getProperty("user.home");
		System.out.println(path);
		factory.setRepository(new File(path));
		factory.setSizeThreshold(1024 * 1024);

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		
		try{
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
					String value = item.getName();
					String suffix = value.substring(value.indexOf("."));
					int start = value.lastIndexOf("\\");
//					获得上传文件名
					String filename = value.substring(start + 1);
//					将文件写到磁盘，如果不解析就成了文件上传功能了
					item.write(new File(path, filename));  //写到服务器
					String uri = path+File.separator+filename, newFileName = UUIDUtil.getUUID()+suffix;
					OSSUtil.uploadFile("maitianditu", newFileName,uri);  //上传到OSS
					File file = new File(uri);
					if(file.isFile() && file.exists()){
						file.delete();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return Result.success();
	}
	
	/**
	 * 获取推荐麦粒
	 * @return
	 */
	@RequestMapping(value="getRecommendGrain.json", method=RequestMethod.POST)
	public @ResponseBody Object getRecommendGrain(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class,Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 获取热门的麦粒推荐给用户
		List<HashMap> data = null;
		try {
			data = grainService.getRecommendGrains("user",userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap resultData = new HashMap();
		resultData.put("grain",data);
		return Result.success(resultData);
	} 
	
	/**
	 * 已阅读麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="read_grain.json", method=RequestMethod.POST)
	public @ResponseBody Object readGrain(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"gid","userId"}, true, new Class[]{Integer.class,Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 	创建recommend
		Long gid = jobj.getLong("gid");
		Recommend recom = null;
		HashMap whereParams = new HashMap();
		whereParams.put("user_id", userId);
		List<Recommend> list = recommendDao.findByShard("recommend", "gid", gid, whereParams);
		if(null == list || list.size() == 0){
			recom = new Recommend();
			recom.setCreateTime(System.currentTimeMillis());
			recom.setGid(jobj.getLong("gid"));
			recom.setUserId(userId);
			recommendDao.saveOrUpdate(recom);
		}
		
		return Result.success();	
	}
	
}
