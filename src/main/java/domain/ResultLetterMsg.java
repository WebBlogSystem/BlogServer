package domain;

public class ResultLetterMsg {
    public boolean isSend;
    public Object res;

    @Override
    public String toString() {
        return "ResultLetterMsg{" +
                "isSend=" + isSend +
                ", res=" + res +
                '}';
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }
}
