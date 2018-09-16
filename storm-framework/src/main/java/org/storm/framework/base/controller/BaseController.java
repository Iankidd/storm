package org.storm.framework.base.controller;

import com.github.pagehelper.Page;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.annotation.IsSearchCondition;
import org.storm.framework.base.exception.BusinessException;
import org.storm.framework.base.model.Entity;
import org.storm.framework.base.service.BaseService;
import org.storm.framework.base.util.datatables.DatatablesView;
import org.storm.framework.base.util.datatables.SearchCondition;
import org.storm.framework.base.util.web.EntityUtils;
import org.storm.framework.base.util.web.ErrorUtils;
import org.storm.framework.base.util.web.RequestUtils;
import org.storm.framework.base.util.web.ResponseUtils;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.service.SysServiceErrorLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseController<Te extends Entity, Ts extends BaseService<Te>> {

    // 把日志记录到slf4j中输出
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected SysServiceErrorLogService sysServiceErrorLogService;

    protected abstract String getPrefix();

    protected final String VIEW = "/view";
    protected final String MODEL_NAME = "entity";
    protected final String ERROR = "error";
    protected final String EDIT = getPrefix() + "_edit";
    protected final String LIST = getPrefix() + "_list";

    protected abstract Ts getBaseService();

    protected abstract Te getBaseEntity();

    protected Te entity;

    @RequestMapping("/list.action")
    public ModelAndView list(HttpServletRequest request, Model model) {
        return new ModelAndView(LIST);
    }

    /**
     * 通用分页方法
     *
     * @param request
     * @return
     */
    @RequestMapping("/pageJson.action")
    @ResponseBody
    public String pageJson(HttpServletRequest request, @IsSearchCondition SearchCondition condition) {
        Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
        logger.info("params:" + paramMap.toString());

        int draw = condition.getDraw();
        int pageNo = condition.getStart();
        int pageSize = condition.getLength();
        int columnIndex = condition.getOrders().get(0).getColumn();
        String sort = condition.getColumns().get(columnIndex).getData();
        String order = condition.getOrders().get(0).getDir();

        if (StringUtils.isNotBlank(sort)) {
            paramMap.put("sort", sort);
            paramMap.put("order", order);
        }

        long filtered = this.getBaseService().getCount(paramMap);
        List<Te> list = this.getBaseService().getPageList(paramMap, pageNo, pageSize);
        Page page = (Page) list;
        long total = page.getTotal();

        DatatablesView<Te> view = new DatatablesView<>();
        view.setDraw(draw);
        view.setRecordsTotal(total);
        view.setRecordsFiltered(filtered);
        view.setData(list);

        return JSONObject.fromObject(view).toString();
    }

    @RequestMapping(value = "/edit.action")
    public ModelAndView edit(@RequestParam(value = "id", required = false) Long id, HttpServletRequest request, Model model) {
        String viewType = request.getParameter("viewType");
        if (id != null && id > 0) {
            Te te = this.getBaseService().getById(id);
            model.addAttribute(MODEL_NAME, te);
            logger.warn("entity:" + JSONObject.fromObject(te));
        }
        model.addAttribute("viewType", viewType);
        return new ModelAndView(EDIT);
    }

    @RequestMapping("/save.action")
    @ResponseBody
    public String save(HttpServletRequest request) throws BusinessException {
        Map<String, Object> params = RequestUtils.getParameterByEdit(request);
        entity = getBaseEntity();
        logger.info("params:" + params.toString());
        EntityUtils.populate(entity, params);
        logger.info("entity:" + JSONObject.fromObject(entity));
        Date nowDate = new Date();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (entity.getId() != null && entity.getId() > 0) {
            Te existEntity = this.getBaseService().getById(entity.getId());
            EntityUtils.populate(existEntity, params);
            existEntity.setModifyDatetime(nowDate);
            if (user != null) {
                existEntity.setModifyUserId(user.getId());
            }
            this.getBaseService().update(existEntity);
        } else {
            if (user != null) {
                entity.setCreateUserId(user.getId());
                entity.setModifyUserId(user.getId());
            }
            entity.setCreateDatetime(nowDate);
            entity.setModifyDatetime(nowDate);
            this.getBaseService().save(entity);
        }

        return ResponseUtils.responseJsonResult(true);
    }

    @RequestMapping("/delete.action")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        if (StringUtils.isNotBlank(ids)) {
            if (ids.indexOf(",") >= 0) {
                this.getBaseService().deleteByIds(ids);
            } else {
                this.getBaseService().deleteById(Long.parseLong(ids));
            }

        }
        return ResponseUtils.responseJsonResult(true);
    }

    @ExceptionHandler
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, Model model, Exception e) {
        try {
            ErrorUtils.excuteError("后台管理", null, null, e, request, sysServiceErrorLogService);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrintWriter writer = null;

        // json格式的ajax请求
        if ((request.getHeader("accept") != null && request.getHeader("accept").indexOf("application/json") > -1)
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
            response.setStatus(500);
            response.setContentType("application/json;charset=utf-8");
            try {
                writer = response.getWriter();
                writer.write("服务器处理错误");
                writer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (writer != null)
                    writer.close();
            }
            return null;
        }
        // 普通请求
        else {
            model.addAttribute("exceptionMsg", e.getMessage());
            return new ModelAndView(ERROR);
        }
    }

    private String getFileMB(long byteFile) {
        if (byteFile == 0)
            return "0MB";
        long mb = 1024 * 1024;
        return "" + byteFile / mb + "MB";
    }

    /**
     * 设置分页控件参数
     *
     * @param list
     * @param request
     */
    public void setPageTagParam(List<Te> list, HttpServletRequest request) {
        if (list instanceof Page) {
            Page page = (Page) list;
            request.setAttribute("page", page.getPageNum());
            request.setAttribute("totalCount", page.getTotal());
            request.setAttribute("totalPage", page.getPages());
            request.setAttribute("pageSize", page.getPageSize());
        }
    }
}
