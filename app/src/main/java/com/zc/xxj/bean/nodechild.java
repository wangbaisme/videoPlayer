package com.zc.xxj.bean;

public class nodechild {
    private String id;
    private String name;
    private String buteid;
    private String butetype;
    private String nodetype;
    private String ispass;

    public String getNodetype() {
        return nodetype;
    }

    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    public nodechild(String id, String name, String buteid, String butetype, String nodetype) {
        super();
        this.id = id;
        this.name = name;
        this.buteid = buteid;
        this.butetype = butetype;
        this.nodetype = nodetype;

    }

    public nodechild(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public nodechild(String id, String name, String ispass) {
        super();
        this.id = id;
        this.name = name;
        this.ispass = ispass;
    }

    public String getIspass() {
        return ispass;
    }

    public void setIspass(String ispass) {
        this.ispass = ispass;
    }

    public String getButeid() {
        return buteid;
    }

    public void setButeid(String buteid) {
        this.buteid = buteid;
    }

    public String getButetype() {
        return butetype;
    }

    public void setButetype(String butetype) {
        this.butetype = butetype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
