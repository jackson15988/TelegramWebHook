package telegram.vo;


import lombok.Data;

@Data
public class MultipOrderDetailVO {


    private String symbol;

    private String tp;

    private String sl;

    private String target;

    private String orderMagicNumber;
}
