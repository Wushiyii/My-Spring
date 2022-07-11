# My-Spring

仅使用Java实现的IOC + DI + MVC框架，所有注解与Spring一致
造轮子只是为了学习Spring的原理，比如BeanFactory的实现

#### 启动服务
`MVC.run(xxx.class)`

#### 启动流程
```java
private void start(Configuration configuration) {
        try {
            //构造一个默认全局配置
            MVC.configuration = configuration;
            //扫描路径
            String basePackageName = configuration.getBootClass().getPackage().getName();
            //通过注解反射进行bean加载
            BeanContainer.getInstance().loadBeans(basePackageName);
            //AOP服务处理
            new AopHandler().init();
            //IOC服务处理
            new IocHandler().init();
            //启动内嵌tomcat服务
            server = new TomcatServer(configuration);
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail to start up , e:", e);
        }
    }
```

#### Controller代码
```java
@Data
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;

    public void fun() {
        testService.fun();
    }
    @ResponseBody
    @RequestMapping("returnFun")
    public String returnFun() {
        return testService.returnFun("hello");
    }

}
```
与Spring中语法几乎一致，不过没有支持模板引擎(`Themeleaf`,`ModelAndView`等)




