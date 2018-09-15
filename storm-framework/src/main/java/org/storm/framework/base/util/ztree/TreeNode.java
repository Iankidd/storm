package org.storm.framework.base.util.ztree;

/**
 * zTree对象
 */
public class TreeNode {

    private Long id;
    private long pId;
    private String name;
    private boolean open;
    private boolean checked;
    private String icon;
    private String iconOpen;
    private String iconClose;
    private boolean nocheck;
    private boolean chkDisabled;
    private boolean isParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconOpen() {
        return iconOpen;
    }

    public void setIconOpen(String iconOpen) {
        this.iconOpen = iconOpen;
    }

    public String getIconClose() {
        return iconClose;
    }

    public void setIconClose(String iconClose) {
        this.iconClose = iconClose;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }


    public boolean isChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }


    public TreeNode(Long id, long pId, String name, boolean open,
                    boolean checked) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.checked = checked;
    }


    public TreeNode(Long id, long pId, String name, boolean open,
                    boolean checked, boolean chkDisabled) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.checked = checked;
        this.chkDisabled = chkDisabled;
    }


    public TreeNode(Long id, long pId, String name, boolean isParent) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.isParent = isParent;
    }

    public TreeNode() {
        super();
    }
}
