package com.mmk.rbac.util;

import java.util.HashMap;

/**
 *
 * @author 大漠穷秋
 */
public class AjaxResult extends HashMap<String,Object> {
    public AjaxResult(){
        super.put("success",true);
        super.put("msg","");
        super.put("data",null);
    }

    public AjaxResult(boolean success){
        super.put("success",success);
    }

    public AjaxResult(String msg){
        super.put("msg",msg);
    }

    public AjaxResult(Object data){
        super.put("data",data);
    }

    public AjaxResult(boolean success, String msg){
        super.put("success",success);
        super.put("msg",msg);
    }

    public AjaxResult(boolean success, Object data){
        super.put("success",success);
        super.put("data",data);
    }

    public AjaxResult(boolean success, String msg, Object data){
        super.put("success",success);
        super.put("msg",msg);
        super.put("data",data);
    }

    public static AjaxResult success(){
        return new AjaxResult(true);
    }

    public static AjaxResult success(String msg){
        return new AjaxResult(true,msg);
    }

    public static AjaxResult success(Object data){
        return new AjaxResult(true,data);
    }

    public static AjaxResult failure(){
        return new AjaxResult(false);
    }

    public static AjaxResult failure(String msg){
        return new AjaxResult(false,msg);
    }

    public static AjaxResult failure(Object msg){
        return new AjaxResult(false,msg);
    }
}
