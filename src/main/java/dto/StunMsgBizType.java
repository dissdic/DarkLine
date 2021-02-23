package dto;

public enum StunMsgBizType {

     NAT_ADDRESS(1),
     PORT_LIMIT(2),
     CHANGE_PORT(3);
     public int type;
     StunMsgBizType(int type){
         this.type = type;
     }
}
