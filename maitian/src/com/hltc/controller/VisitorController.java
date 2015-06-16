package com.hltc.controller;




import static com.hltc.util.SecurityUtil.parametersValidate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.IVisitorDao;
import com.hltc.dao.IVrecommendDao;
import com.hltc.entity.Grain;
import com.hltc.entity.Visitor;
import com.hltc.entity.Vrecommend;
import com.hltc.service.IGrainService;
import com.hltc.util.CommonUtil;

/**
 *游客模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/visitor")
public class VisitorController {
	@Autowired IVisitorDao visitorDao;
	@Autowired IGrainDao grainDao;
	@Autowired IGrainService grainService;
	@Autowired IVrecommendDao vrecommendDao;
	/**
	 * 获取一个游客id
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/getVid.json", method=RequestMethod.GET)
	public @ResponseBody Object getVid(HttpServletRequest request){
		Long ip = CommonUtil.ip2long(CommonUtil.getRemortIP(request));
		
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		HashMap data = new HashMap();
		
		try{
			Integer count = visitorDao.countOfOneIp(CommonUtil.ip2long(CommonUtil.getRemortIP(request)), date.getTime());
			if(null == count) return Result.fail(ErrorCode.DB_ERROR);
			if(count > GlobalConstant.VISITOR_LIMIT_COUNT) return Result.fail(ErrorCode.VISITOR_COUNT_LIMIT_ERROR);
			
			Visitor v = new Visitor();
			v.setIp(ip);
			v.setCreateTime(System.currentTimeMillis());
			v = visitorDao.saveOrUpdateWithBack(v);
			data.put("vid", v.getVid());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return Result.success(data);
	}
	
	/**
	 * 获取推荐麦粒
	 * @return
	 */
	@RequestMapping(value="getRecommendGrain.json", method=RequestMethod.POST)
	public @ResponseBody Object getRecommendGrain(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "vid", true, new Class[]{Integer.class,Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 获取热门的麦粒推荐给游客
		List<HashMap> data = null;
		try {
			data = grainService.getRecommendGrains("visitor",jobj.getLong("vid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap resultData = new HashMap();
		resultData.put("grain",data);
		return Result.success(resultData);
	} 
	
	@RequestMapping(value="read_grain.json", method=RequestMethod.POST)
	public @ResponseBody Object readGrain(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"gid","vid"}, true, new Class[]{Integer.class,Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 	创建vrecommend
		Long gid = jobj.getLong("gid");
		Long vid = jobj.getLong("vid");
		Vrecommend vrecom = null;
		HashMap whereParams = new HashMap();
		whereParams.put("vid", vid);
		List<Vrecommend> list = vrecommendDao.findByShard("vrecommend", "gid", gid, whereParams);
		if(null == list || list.size() == 0){
			vrecom = new Vrecommend();
			vrecom.setCreateTime(System.currentTimeMillis());
			vrecom.setGid(jobj.getLong("gid"));
			vrecom.setVid(jobj.getLong("vid"));
			vrecommendDao.saveOrUpdate(vrecom);
		}
		
		return Result.success();	
	}
}
