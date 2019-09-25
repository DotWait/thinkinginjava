package com.dotwait.xml;

import kotlin.jvm.Synchronized;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Xml {
    @Test
    public void convertToXml() throws IOException {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        root.addComment("this is a comment");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");//根据需要设置编码
        XMLWriter writer = new XMLWriter(System.out, format);
        document.normalize();
        writer.write(document);
        writer.close();
    }

    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     * <env:Envelope xmlns:env="http://www.w3.org/2003/05/soap-envelope">
     *     <env:Header>
     *         <auth:Authentication xmlns:auth="http://www.h3c.com/netconf/base:1.0" env:mustUnderstand="true">
     *             <auth:AuthInfo>100002970e66cf2b756f689e4cef8e6da600</auth:AuthInfo>
     *         </auth:Authentication>
     *     </env:Header>
     *     <env:Body>
     *         <rpc-reply
     * 			xmlns="urn:ietf:params:xml:ns:netconf:base:1.0"
     * 			xmlns:web="urn:ietf:params:xml:ns:netconf:base:1.0" message-id="101">
     * 			<ok/>
     * 		</rpc-reply>
     *     </env:Body>
     * </env:Envelope>
     * @throws DocumentException
     */
    @Test
    public void parseText() throws DocumentException, IOException {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <env:Header>\n" +
                "        <auth:Authentication xmlns:auth=\"http://www.h3c.com/netconf/base:1.0\" env:mustUnderstand=\"true\">\n" +
                "            <auth:AuthInfo>100002970e66cf2b756f689e4cef8e6da600</auth:AuthInfo>\n" +
                "        </auth:Authentication>\n" +
                "    </env:Header>\n" +
                "    <env:Body>\n" +
                "        <rpc-reply\n" +
                "\t\t\txmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\"\n" +
                "\t\t\txmlns:web=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"101\">\n" +
                "\t\t\t<ok/>\n" +
                "\t\t</rpc-reply>\n" +
                "    </env:Body>\n" +
                "</env:Envelope>";
        Document document = DocumentHelper.parseText(text);
        Element root = document.getRootElement();
        System.out.println(root);
        List<Element> elements = root.elements();
        for (Element element : elements){
            System.out.println(element.getName());
        }
        Element header = root.element("Header");
        System.out.println(header);
        Element body = root.element("Body");
        System.out.println(body);
        System.out.println(body.getNamespacePrefix());
        Element authentication = header.element("Authentication");
        Element authInfo = authentication.element("AuthInfo");
        System.out.println(authInfo.getText());
        System.out.println(authInfo.getData());


        String xml = document.asXML();
        System.out.println(xml);

        /*输出到控制台*/
/*        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");//根据需要设置编码
        XMLWriter writer = new XMLWriter(System.out, format);
        document.normalize();
        writer.write(document);
        writer.close();*/
    }

    @Test
    public void parseTextTest() throws DocumentException {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\t<root>\n" +
                "\t\t<first a=\"123\">\n" +
                "\t\t\t<second b=\"456\">\n" +
                "\t\t\tthis is data!\n" +
                "\t\t\t</second>\n" +
                "\t\t</first>\n" +
                "\t</root>";
        Document document = DocumentHelper.parseText(text);
        Element root = document.getRootElement();
        System.out.println(root.getName());
        Element first = root.element("first");
        System.out.println(first);
        Element second = first.element("second");
        System.out.println(second);
        System.out.println(second.getText());
        Element others = first.addElement("others");
        others.addAttribute("other", "789");
        others.addText("the default result");
        String s = document.asXML();
        System.out.println(s);
    }

    @Test
    public void generateXml(){
        Document rpc = DocumentHelper.createDocument(DocumentHelper.createElement("rpc"));
        String s = rpc.asXML();
        System.out.println(s);
    }

    @Test
    public void test(){
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void process(){
        System.out.println("start process");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finish process");
    }

    @Test
    public void xmlTest() throws DocumentException {
        String header = "<root><hello>123</hello><header><add><this></this></add></header></root>";
        String body = "<body><test>111</test></body>";
        Document document = DocumentHelper.parseText(header);
        Element root = document.getRootElement();
        System.out.println(root);
        Element hello = root.element("hello");
        hello.setText("456");
        hello.addAttribute("attr1", "111");
        hello.addAttribute("attr2", "222");
        hello.addAttribute("attr3", "222");
        System.out.println(hello.getText());
        Element header1 = root.element("header");
        Element add = header1.element("add");
        Element aThis = add.element("this");
        System.out.println(aThis);
        Document bodyDocument = DocumentHelper.parseText(body);
        Element rootElement = bodyDocument.getRootElement();

        System.out.println(document.asXML());
    }
}
