package com.bastion.master.protocol;

public class PacketParserTest {

    public static void main(String[] args) {
        String s = "TYPE:HEARTBEAT|FROM:node1|DATA:OK";

        //regex escaping
        String[] incoming = s.split("\\|");

        String   type = null;
        String from = null;
        String data = null;

        for(String str : incoming){
            System.out.println(str);

            if(str.startsWith("TYPE:")){
                //by default assumes to go till end
                type = (str.substring("TYPE:".length()));
            }
            if(str.startsWith("FROM:")){
                //by default assumes to go till end
                from = (str.substring("FROM:".length()));
            }
            if(str.startsWith("DATA:")){
                //by default assumes to go till end
                data = (str.substring("DATA".length()));
            }
        }

        Packet packet = new Packet(PacketType.valueOf(type), from, data);

    }
    

}
