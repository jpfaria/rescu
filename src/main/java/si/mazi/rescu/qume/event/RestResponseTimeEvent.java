package si.mazi.rescu.qume.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestResponseTimeEvent {

    public static final String HTTPS_SCHEME = "https";
    public static final String HTTP_SCHEME = "http";

    private String host;
    private String method;
    private Double responseTime;
    private Integer port;
    private String scheme;

    public Integer getPort() {

        if (scheme.equals(HTTPS_SCHEME)) {
            return 443;
        } else if (scheme.equals(HTTP_SCHEME)) {
            return 80;
        }

        return port;
    }

}
