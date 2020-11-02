package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum ProcessState {
    INIT("INIT", "init"),
    STARTED("STARTED", "started"),
    COMPLETED("COMPLETED", "completed"),
    FAILED("FAILED", "failed"),
    ABORTED("ABORTED", "aborted"),
    FORK("FORK", "fork"),
    KILLED("KILLED", "killed"),
    SUBMITTED("SUBMITTED", "submitted"),
    ACCEPTED("ACCEPTED", "accepted");

    private final String value;
    private final String text;

    private ProcessState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static ProcessState selectGender(String name) {
        for (ProcessState portType : ProcessState.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }
}
