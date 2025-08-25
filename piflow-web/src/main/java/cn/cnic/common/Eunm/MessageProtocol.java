package cn.cnic.common.Eunm;

/**
 * 消息队列协议类型枚举。
 * 用于区分不同的消息队列服务。
 */
public enum MessageProtocol {
    RABBITMQ("RABBITMQ", "RabbitMQ Message Queue"),
    KAFKA("KAFKA", "Apache Kafka"),
    ;

    private final String name;
    private final String description;

    MessageProtocol(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static MessageProtocol fromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (MessageProtocol protocol : MessageProtocol.values()) {
            if (protocol.getName().equalsIgnoreCase(name.trim())) {
                return protocol;
            }
        }
        return null;
    }
}