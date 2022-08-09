package de.gommzy.applicationex.groups;

public class MemberData {
    public String uuid;
    public Group group;
    public long duration;

    public MemberData(String uuid, Group group, long duration) {
        this.uuid = uuid;
        this.group = group;
        this.duration = duration;
    }
}
