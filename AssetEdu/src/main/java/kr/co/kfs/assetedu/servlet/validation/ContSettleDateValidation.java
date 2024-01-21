package kr.co.kfs.assetedu.servlet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import kr.co.kfs.assetedu.model.Opr01Cont;

public class ContSettleDateValidation implements ConstraintValidator<ContSettleDate, Opr01Cont>{

	@Override
	public boolean isValid(Opr01Cont cont, ConstraintValidatorContext context) {
	
		String contDate = cont.getOpr01ContDate();
		String settleDate = cont.getOpr01SettleDate();
		if(contDate == null || settleDate == null ) return false; 
		try{
            int iContDate = Integer.parseInt(contDate.replaceAll("\\D", ""));
            int iSettleDate = Integer.parseInt(settleDate.replaceAll("\\D", ""));
            return iContDate <= iSettleDate; 
        }
        catch (NumberFormatException ex){
            return false;
        }
	}

}
