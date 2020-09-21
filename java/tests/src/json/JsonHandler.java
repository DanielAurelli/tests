package json;

import java.io.StringReader;

import javax.json.*;
import javax.json.stream.*;
import javax.json.stream.JsonParser.Event;

public class JsonHandler {

    public static void main(String[] args) {
        String cmd=
                "{\n\"library\":\"SAMPLES\",\"name\":\"ADSPSUM\",\n\"stmt\":[\n {\"offset\":0,\"source\":\"ADSPSUM \",\"line\":10,\"col\":0,\"level\":0,\"keyword\":\"INPUT\"}\n,{\"offset\":10,\"source\":\"ADSPSUM \",\"line\":11,\"col\":0,\"level\":0,\"keyword\":\"CALLNAT\"}\n,{\"offset\":20,\"source\":\"ADSPSUM \",\"line\":12,\"col\":6,\"level\":0,\"keyword\":\"WRITE\"}\n  ]\n,\"var\":[\n {\"id\":0,\"type\":0,\"level\":1,\"name\":\"#N1\",\"format\":\"(N2.0)\"}\n,{\"id\":1,\"type\":0,\"level\":1,\"name\":\"#N2\",\"format\":\"(N2.0)\"}\n,{\"id\":2,\"type\":0,\"level\":1,\"name\":\"#R\",\"format\":\"(N4.0)\"}\n  ]\n,\"sym\":[\n  ]\n}";
                // "{ \"library\":\"HR304011\",\"name\":\"BRH11510\",\"path\":\"C:\\Consist\\application\\HR304011\\SRC\\BRH11510.NSP\" ,\"stmt\":[ {\"offset\":0000,\"source\":\"CRHSTDPF\",\"line\":0012,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0006,\"source\":\"CRHSTDPF\",\"line\":0013,\"col\":07,\"level\":1,\"keyword\":\"SET KEY\"} ,{\"offset\":0037,\"source\":\"CRHSTDPF\",\"line\":0017,\"col\":07,\"level\":1,\"keyword\":\"SET KEY\"} ,{\"offset\":0066,\"source\":\"BRH11510\",\"line\":0076,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0072,\"source\":\"BRH11510\",\"line\":0077,\"col\":03,\"level\":1,\"keyword\":\"INPUT\"} ,{\"offset\":0118,\"source\":\"BRH11510\",\"line\":0083,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0126,\"source\":\"BRH11510\",\"line\":0084,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0130,\"source\":\"BRH11510\",\"line\":0087,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0137,\"source\":\"BRH11510\",\"line\":0088,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0144,\"source\":\"BRH11510\",\"line\":0090,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0149,\"source\":\"BRH11510\",\"line\":0093,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0153,\"source\":\"BRH11510\",\"line\":0094,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0158,\"source\":\"BRH11510\",\"line\":0095,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0163,\"source\":\"BRH11510\",\"line\":0096,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0168,\"source\":\"BRH11510\",\"line\":0097,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0172,\"source\":\"BRH11510\",\"line\":0098,\"col\":00,\"level\":0,\"keyword\":\"COMPUTE\"} ,{\"offset\":0189,\"source\":\"BRH11510\",\"line\":0115,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0194,\"source\":\"BRH11510\",\"line\":0116,\"col\":00,\"level\":0,\"keyword\":\"CALLNAT\"} ,{\"offset\":0218,\"source\":\"BRH11510\",\"line\":0123,\"col\":00,\"level\":0,\"keyword\":\"MOVE\"} ,{\"offset\":0225,\"source\":\"BRH11510\",\"line\":0125,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0232,\"source\":\"BRH11510\",\"line\":0126,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0237,\"source\":\"BRH11510\",\"line\":0127,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0242,\"source\":\"BRH11510\",\"line\":0128,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0247,\"source\":\"BRH11510\",\"line\":0129,\"col\":03,\"level\":1,\"keyword\":\"COMPUTE\"} ,{\"offset\":0258,\"source\":\"BRH11510\",\"line\":0131,\"col\":03,\"level\":1,\"keyword\":\"MOVE\"} ,{\"offset\":0263,\"source\":\"BRH11510\",\"line\":0132,\"col\":11,\"level\":1,\"keyword\":\"PERFORM\"} ,{\"offset\":0267,\"source\":\"BRH11510\",\"line\":0134,\"col\":11,\"level\":1,\"keyword\":\"PERFORM\"} ,{\"offset\":0269,\"source\":\"BRH11510\",\"line\":0137,\"col\":00,\"level\":0,\"keyword\":\"IF\"} ,{\"offset\":0276,\"source\":\"BRH11510\",\"line\":0138,\"col\":03,\"level\":1,\"keyword\":\"SKIP\"} ,{\"offset\":0279,\"source\":\"BRH11510\",\"line\":0139,\"col\":03,\"level\":1,\"keyword\":\"NEWPAGE\"} ,{\"offset\":0282,\"source\":\"BRH11510\",\"line\":0140,\"col\":11,\"level\":1,\"keyword\":\"WRITE\"} ,{\"offset\":5592,\"source\":\"BRH11510\",\"line\":0954,\"col\":08,\"level\":0,\"keyword\":\"WRITE\"} ] ,\"var\":[ {\"id\":0,\"type\":2,\"level\":1,\"name\":\"##AUT-SUBM-BATCH\",\"format\":\"(A1)\"} ,{\"id\":1,\"type\":2,\"level\":1,\"name\":\"##COD-SEG\",\"format\":\"(A15)\"} ,{\"id\":2,\"type\":2,\"level\":1,\"name\":\"##COD-USUARIO\",\"format\":\"(A8)\"} ,{\"id\":3,\"type\":2,\"level\":1,\"name\":\"##CD-CLI-USU\",\"format\":\"(A10)\"} ,{\"id\":4,\"type\":2,\"level\":2,\"name\":\"##CD-CLI-USU-PREF1\",\"format\":\"(A7)\"} ,{\"id\":5,\"type\":2,\"level\":2,\"name\":\"##CD-CLI-USU-FILL1\",\"format\":\"(A3)\"} ,{\"id\":6,\"type\":2,\"level\":2,\"name\":\"##CD-CLI-USU-PREF2\",\"format\":\"(A9)\"} ,{\"id\":7,\"type\":2,\"level\":2,\"name\":\"##CD-CLI-USU-FILL2\",\"format\":\"(A1)\"} ,{\"id\":8,\"type\":2,\"level\":1,\"name\":\"##IN-NIVEL-BLOQ-USU\",\"format\":\"(N1.0)\"} ,{\"id\":9,\"type\":2,\"level\":1,\"name\":\"##IND-EST\",\"format\":\"(N1.0)\"} ,{\"id\":10,\"type\":2,\"level\":1,\"name\":\"##PF\",\"format\":\"(A5)\",\"ndims\":\"1\",\"dim\":\"1:8\"} ,{\"id\":491,\"type\":0,\"level\":1,\"name\":\"#MSG-PARAM\",\"format\":\"(N4.0)\"} ,{\"id\":492,\"type\":0,\"level\":1,\"name\":\"Decide/1\",\"format\":\"(I4)\"} ] ,\"sym\":[ {\"id\":0,\"value\":\"RH_DADOS_PESSOAIS\"} ,{\"id\":1,\"value\":\"RH_DEPENDENTE\"} ,{\"id\":2,\"value\":\"RH_EMPRESA\"} ,{\"id\":3,\"value\":\"RH_FUNC_ADICIONAIS\"} ,{\"id\":4,\"value\":\"RH_FUNC_COMPLEMENT\"} ,{\"id\":5,\"value\":\"RH_FUNC_MULTI\"} ,{\"id\":6,\"value\":\"RH_FUNCIONARIO\"} ,{\"id\":7,\"value\":\"RH_OCOR_ADMINISTR\"} ,{\"id\":8,\"value\":\"RH_PARM_INSTAL\"} ,{\"id\":9,\"value\":\"RH_PENSIONISTA\"} ,{\"id\":10,\"value\":\"RH_VERBAS_FIXAS\"} ,{\"id\":11,\"value\":\"RH_PARAM_SMT\"} ] }";
        JsonObject jo = Json.createReader(new StringReader(cmd)).readObject();
        System.out.println(cmd);
        System.out.println(jo.getJsonArray("var").toString());
        
        JsonParserFactory factory = Json.createParserFactory(null);
        JsonParser parser = factory.createParser(new StringReader(cmd));

        while (parser.hasNext()) {
          Event event = parser.next();
          System.out.println(event.name());
          switch (event) {
            case KEY_NAME: {
              System.out.print(parser.getString() + "="); break;
            }
            case VALUE_STRING: {
                System.out.println(parser.getString()); break;
              }
            case VALUE_NUMBER: {
                System.out.println(parser.getInt()); break;
              }
          }
        }
        
        System.out.println(Json.createObjectBuilder()
        .add("subject", "person")
        .add("content", Json.createObjectBuilder()
                .add("name", "JUCA")
                .build())
        .build().toString());

    }

}
