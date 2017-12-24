package jp.chang.myclinic.hotline;

public class Context {

    public static Context INSTANCE;
    private User sender;
    private User recipient;

    public Context(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
