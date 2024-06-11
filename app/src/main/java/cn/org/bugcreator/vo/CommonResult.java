package cn.org.bugcreator.vo;


import java.io.Serializable;

/**
 * @author aiden
 * @data 11/06/2024
 * @description
 */
public class CommonResult implements Serializable {

    private boolean flag;

    private String message;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
