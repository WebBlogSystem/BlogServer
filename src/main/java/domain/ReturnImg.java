package domain;

import java.util.Arrays;

public class ReturnImg {
    private String errno;
    private String[] data;

    @Override
    public String toString() {
        return "ReturnImg{" +
                "errno='" + errno + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
