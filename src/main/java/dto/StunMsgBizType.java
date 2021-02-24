package dto;

public enum StunMsgBizType {

     NAT_ADDRESS(1),
     SERVER_CHANGE_PORT(2),
     CLIENT_CHANGE_PORT(3),

     SERVER_CHANGE_HOST(4),
     CLIENT_CHANGE_HOST(5),

     CONFIRM(6);
     public int type;
     StunMsgBizType(int type){
         this.type = type;
     }
}
