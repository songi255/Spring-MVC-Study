package detail.profile;

import org.springframework.beans.factory.annotation.Value;

public class Info {
    @Value("${info.version}")
    private String version;

    public void printInfo(){
        System.out.println("version = " + version);
    }

    @Value("${info.version}") // 이렇게 setter 에 적용할 수도 있다.
    public void setVersion(String version){
        this.version = version;
    }
}
