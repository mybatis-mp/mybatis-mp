package cn.mybatis.mp.generator.config;

import lombok.Getter;

@Getter
public class ActionConfig {

    /**
     * 是否启用
     */
    private boolean enable = true;


    /**
     * 控制器父类
     */
    private String superClass;

    /**
     * 注入service
     */
    private boolean injectService = true;

    /**
     * 是否含有泛型
     */
    private boolean generic;

    /**
     * 新增
     */
    private boolean save = true;

    /**
     * 新增的方法名
     */
    private String saveMethodName = "save";

    /**
     * 新增的path
     */
    private String saveUriPath = "/save";

    /**
     * 修改
     */
    private boolean update = true;

    /**
     * 修改的方法名
     */
    private String updateMethodName = "update";

    /**
     * 修改的path
     */
    private String updateUriPath = "/update";

    /**
     * 删除
     */
    private boolean deleteById = true;

    /**
     * 删除的方法名
     */
    private String deleteByIdMethodName = "deleteById";

    /**
     * 删除的path
     */
    private String deleteByIdUriPath = "/deleteById";

    /**
     * 分页
     */
    private boolean find = true;

    /**
     * 分页的方法名
     */
    private String findMethodName = "find";

    /**
     * 分页的path
     */
    private String findUriPath = "/find";

    /**
     * 单个查询
     */
    private boolean getById = true;

    /**
     * 单个查询的方法名
     */
    private String getByIdMethodName = "getById";

    /**
     * 单个查询的path
     */
    private String getByIdUriPath = "/getById";

    /**
     * 实体类包名
     */
    private String packageName = "action";

    /**
     * mapper后缀
     */
    private String suffix = "Action";

    /**
     * save update 等返回的类型
     */
    private String returnClass;

    /**
     * 返回的名字
     */
    private String returnClassName = "Object";

    /**
     * 是否开启 swagger
     */
    private boolean swagger = false;

    /**
     * 设置是否启用
     */
    public ActionConfig enable(boolean enable) {
        this.enable = enable;
        return this;
    }

    /**
     * 控制器父类
     */
    public ActionConfig superClass(String superClass) {
        this.superClass = superClass;
        return this;
    }

    /**
     * 是否注入service
     *
     * @param injectService
     * @return
     */
    public ActionConfig injectService(boolean injectService) {
        this.injectService = injectService;
        return this;
    }

    /**
     * 启用泛型
     */
    public ActionConfig generic(boolean generic) {
        this.generic = generic;
        return this;
    }

    /**
     * 是否生成save方法
     *
     * @param save
     * @return
     */
    public ActionConfig save(boolean save) {
        this.save = save;
        return this;
    }

    public ActionConfig saveMethodName(String saveMethodName){
        this.saveMethodName = saveMethodName;
        return this;
    }

    public ActionConfig saveUriPath(String saveUriPath) {
        this.saveUriPath = saveUriPath;
        return this;
    }

    /**
     * 是否生成update方法
     *
     * @param update
     * @return
     */
    public ActionConfig update(boolean update) {
        this.update = update;
        return this;
    }
    public ActionConfig updateMethodName(String updateMethodName) {
        this.updateMethodName = updateMethodName;
        return this;
    }

    public ActionConfig updateUriPath(String updateUriPath) {
        this.updateUriPath = updateUriPath;
        return this;
    }
    /**
     * 是否生成deleteById方法
     *
     * @param deleteById
     * @return
     */
    public ActionConfig deleteById(boolean deleteById) {
        this.deleteById = deleteById;
        return this;
    }

    public ActionConfig deleteByIdMethodName(String deleteByIdMethodName) {
        this.deleteByIdMethodName = deleteByIdMethodName;
        return this;
    }

    public ActionConfig deleteByIdUriPath(String deleteByIdUriPath) {
        this.deleteByIdUriPath = deleteByIdUriPath;
        return this;
    }

    /**
     * 是否生成find方法
     *
     * @param find
     * @return
     */
    public ActionConfig find(boolean find) {
        this.find = find;
        return this;
    }
    public ActionConfig findMethodName(String findMethodName) {
        this.findMethodName = findMethodName;
        return this;
    }

    public ActionConfig findUriPath(String findUriPath) {
        this.findUriPath = findUriPath;
        return this;
    }

    /**
     * 是否生成getById方法
     *
     * @param getById
     * @return
     */
    public ActionConfig getById(boolean getById) {
        this.getById = getById;
        return this;
    }
    public ActionConfig getByIdMethodName(String getByIdMethodName) {
        this.getByIdMethodName = getByIdMethodName;
        return this;
    }

    public ActionConfig getByIdUriPath(String getByIdUriPath) {
        this.getByIdUriPath=getByIdUriPath;
        return this;
    }

    /**
     * 控制器的包名
     *
     * @param packageName
     * @return
     */
    public ActionConfig packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * 控制器的后缀
     *
     * @param suffix
     * @return
     */
    public ActionConfig suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 控制器save,update,...等返回的类
     *
     * @param returnClass
     * @return
     */
    public ActionConfig returnClass(String returnClass) {
        this.returnClass = returnClass;
        int dotIndex = returnClass.lastIndexOf(".");
        if (dotIndex > 0) {
            this.returnClassName = returnClass.substring(dotIndex + 1);
        } else {
            this.returnClassName = returnClass;
        }
        return this;
    }

    /**
     * 控制器save,update,...等返回的类
     *
     * @param returnClass
     * @return
     */
    public ActionConfig returnClass(Class returnClass) {
        return this.returnClass(returnClass.getName());
    }

    /**
     * 是否开启 swagger
     *
     * @param enable
     * @return
     */
    public ActionConfig swagger(boolean enable) {
        this.swagger = enable;
        return this;
    }
}
