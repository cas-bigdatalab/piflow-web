package cn.cnic.third.vo.flow;

public class ThirdProgressVo {

    private String appId;
    private String name;
    private String state;
    private String progress;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "ProgressVo [appId=" + appId + ", name=" + name + ", state="
                + state + ", progress=" + progress + "]";
    }


}
