// All the numbers we process are binary, so this file takes care of translating the binary numbers inputed into the 10-base numerical system. Most of the logic in this file concerns rounding, carrys, decimal multiplications/divisions, and signs/negatives.
public class ArbNum{
	
	protected int[] numArray;
	protected boolean sign = false;
	private static final int PRECISION = 16;
	public  int offSet = PRECISION;

//	private int reverse = PRECISION;
	
	// A string, like "3.1415926" or "-1024536". accepts but drops leading zeros
	public ArbNum(String num){
		int secondaryOffset = 0;
		
		numArray = new int[PRECISION];
		
		for(int i=0;i<PRECISION;i++){
			numArray[i] = 0;
		}
		
		int index=PRECISION-1;
		boolean flag = false;
		
		num = num.replaceAll("[^0-9.-]","");
		
		if(num.charAt(0)=='-') sign = true;
	
		num = num.replace("-","");
		
		while(num.charAt(0)=='0' && num.length() > 0) num=num.substring(1);
		
		
		for (int i=0;i < num.length();i++){
		
			if(i>=PRECISION + secondaryOffset) break;
			if(num.charAt(i)!='.'){
				numArray[index--] = num.charAt(i)-'0';
				if(!flag) offSet--;	
			} else {
				flag = true;
				secondaryOffset++;
			}
		}	
	//		while(numArray[PRECISION-1]==0 && offSet < PRECISION){
//			System.out.println("Shifting Left.");
//			shiftLeft();
//		}
	}
	
	public ArbNum(){
		numArray = new int[PRECISION];
		for(int i=0;i<PRECISION;i++) numArray[i] = 0;
		
		
	}
	
	protected ArbNum sub(ArbNum that){
//		diff=this-that;
//		System.out.println("Entering sub method.");
		ArbNum diff = this.copy();
		ArbNum b = that.copy();

//		System.out.println("this:  " + this + "      that:  "+that);
		boolean isRev=false;
		if(diff.sign && b.sign){
			isRev=true;
			diff.sign = b.sign = false;
			return b.sub(diff);
		}else if(diff.sign && !b.sign){
			ArbNum temp;
			diff.sign = false;
			temp = diff.add(b);
			temp.sign = !temp.sign;
			diff.sign = true;
			
			return temp;
			
		}else if(!diff.sign && b.sign){
			ArbNum temp;
			b.sign = false;
			temp = diff.add(b);
			b.sign = true;
			return temp;
		}
		
		if(!diff.isGreaterOrEqual(b)){
			ArbNum temp;
			temp = b.sub(diff);
			temp.sign = !temp.sign;
			return temp;
		}
		
//		ArbNum diff = new ArbNum();
		
		// This just matches the shifting
		while(diff.offSet>b.offSet) diff.shiftRight();
		while(b.offSet>diff.offSet) b.shiftRight();
		
//		diff.offSet = this.offSet;
		
//		int borrow = 0;
//		for(int i=0;i<PRECISION;i++){
//			diff.numArray[i] = diff.numArray[i] - b.numArray[i] - borrow;
//			if(diff.numArray[i]< 0){
//				diff.numArray[i]+=10;
//				borrow=1;
//			}else{
//				borrow = 0;
//			}
//		}
//
//		while(diff.numArray[PRECISION-1]==0 && diff.offSet < PRECISION){
//			diff.shiftLeft();
//		}
		
//		if(borrow==1){
//			diff.sign=!diff.sign;
//			diff.numArray[PRECISION-1] = 10 - diff.numArray[PRECISION-1];
//		}
		diff.arraySub(b);
		
		if(isRev){
			diff.sign = !diff.sign;
		}
		return diff;
	}
	
	protected void arraySub(ArbNum that){
		int borrow = 0;
		for(int i=0;i<PRECISION;i++){
			this.numArray[i] = this.numArray[i] - that.numArray[i] - borrow;
			if(this.numArray[i]< 0){
				this.numArray[i]+=10;
				borrow=1;
			}else{
				borrow = 0;
			}
		}

		while(this.numArray[PRECISION-1]==0 && this.offSet < PRECISION){
			this.shiftLeft();
		}
	}
	
	protected ArbNum add(ArbNum that){
//		sum=this+that;
//		System.out.println("Entering add method.");
		
		ArbNum sum = this.copy();
		ArbNum b = that.copy();

		
		boolean isRev=false;
		if(this.sign && that.sign){
			isRev=true;
		}else if(this.sign && !that.sign){
			ArbNum temp;
			this.sign = false;
			temp = that.sub(this);
			this.sign = true;
			return temp;
		}else if(!this.sign && that.sign){
			ArbNum temp;
			that.sign = false;
			temp = this.sub(that);
			that.sign = true;
			return temp;
		}
		
		// This just matches the shifting
		while(sum.offSet>b.offSet) sum.shiftRight();
		while(b.offSet>sum.offSet) b.shiftRight();
//		int carry = 0;
//		for(int i=0;i<PRECISION;i++){
//			sum.numArray[i] = this.numArray[i] + that.numArray[i] + carry;
//			if(sum.numArray[i]>=10){
//				sum.numArray[i]-=10;
//				carry=1;
//			}else carry=0;
//		}
//		
//		if(carry == 1){
//			sum.shiftRight();
//			sum.numArray[PRECISION-1] = 1;
//		}

		sum.arrayAdd(b);
		
		if(isRev){
			sum.sign = true;
		}
		return sum;
	}
	
	protected void arrayAdd(ArbNum smallerNum){
//		System.out.println("Entering arrayAdd method.");
		
		int carry = 0;
		//int productIndex = PRECISION-this.offSet;
		for(int i=0;i<PRECISION;i++){
//			System.out.println("first: " + this + "     second: " + smallerNum);
			this.numArray[i] = this.numArray[i] + smallerNum.numArray[i] + carry;
			if(this.numArray[i]>=10){
				this.numArray[i]-=10;
				carry=1;
			}
			else{
			 carry=0;
			}
		}
		if(carry != 0){
			this.shiftRight();
			this.numArray[PRECISION-1] = carry;
		}
		
//		System.out.println("final1: " + this + "     final2: " + smallerNum);

		
	}
	
	protected ArbNum mul(ArbNum that){
//		System.out.println("Entering mul method.");
		//make copies of this and that
		
		ArbNum a = this.copy();
		ArbNum b = that.copy();
//		a.sign = b.sign = false;
		// Value that will store the answer
		ArbNum product = new ArbNum();
		
		product.sign = a.sign = a.sign ^ b.sign;
		
		// check to see if product will be negative
//		if((a.sign && !b.sign) || (!a.sign && b.sign)){
//			product.sign = !product.sign;
//		}
			
		a.offSet = a.offSet - (PRECISION - b.offSet - 1);
		
		for(int i = PRECISION-1; i >= 0; i--){
			while(b.numArray[i] > 0){
//				product.arrayAdd(a);

				product = product.add(a);
				
				b.numArray[i]--;
				
//				System.out.println("A: " + a);
//				System.out.println("B: " + b);
//				System.out.println("PRODUCT: " + product);
			}
			a.div10();
		}
		
		return product;
	}
	
	protected ArbNum div(ArbNum that){
//		System.out.println("Entering div method.");
		
		//make copies of this and that
		ArbNum c = this.copy();
		ArbNum d = that.copy();
		
//		System.out.println("NUM: " + c);
//		System.out.println("DEN: " + d);
		
		//Finds sign of 
				
		// Value that will store the answer
		ArbNum quotient = new ArbNum();	
		
		quotient.sign = c.sign ^ d.sign;
		
		c.sign = d.sign = false;
		
		
		//START THIS ON THURSDAY 3/30, FINISH LOGIC FOR DIV OFFSET
		
		//START THIS ON THURSDAY 3/30, FINISH LOGIC FOR DIV OFFSET
		
		
//		if(d.offSet == PRECISION){
//			d.offSet--;
//			c.offSet--;
//		}
//		if(d.offSet <=  PRECISION - 1){
//			d.offSet++;
//			c.offSet++;
//		}
		if(d.offSet == PRECISION || c.offSet == PRECISION){
			d.offSet--;
			c.offSet--;
		}
		
		while( d.offSet < PRECISION - 1 && c.offSet < PRECISION - 1){
			d.offSet++;
			c.offSet++;
		}
		
		while(d.numArray[PRECISION-1] == 0){
			d.mul10();
			c.offSet--;
		}


		quotient.offSet = c.offSet;
		c.offSet = PRECISION - 1;
//		while(d.offSet!=PRECISION-1){
//		
//			if(d.offSet == PRECISION){
//				d.offSet--;
//				c.offSet--;
//			} else{
//				d.offSet++;
//				c.offSet++;
//			}
//		}
		
				
		// Coming out of this while-loop, our decimals should be correct.
//		
//			System.out.println(" OUT OF LOOP NUM : " + c);
//			System.out.println("OUT OF LOOP DEN : " + d);
//			System.out.println("OUT OF LOOP QUO : " + quotient);
		
		for(int i = PRECISION-1; i >= 0; i--) {
//			System.out.println(i);
			while(c.isGreaterOrEqual(d)){
				c = c.sub(d);
				quotient.numArray[i]++;
//				System.out.println("NUM: " + c);
//				System.out.println("DEN: " + d);
//				System.out.println("QUOTIENT: " + quotient);
			}
			if(c.numArray[PRECISION - 1] == 0){
				c.shiftLeft();
			}
			c.offSet--;	
			if(i == PRECISION - 1 && quotient.numArray[i] == 0){
				quotient.offSet++;
				i++;
			}
		}
		
		while(quotient.offSet > PRECISION){
			quotient.shiftRight();
		}
		/*
			*For decimal point, however many times you shift the decimal point to the right,
			move it back that many times in relation to the offset
			
			*Decimal point and digits are two seperate methods, they don't rely on eachother
			if 0 < d < 1, 
		    Pseduo Code: "c" divided by "d":
		       1. For each digit in arbNum (precision -> 0)
		       		a) while "d" >= "c" 
		       			i) subtract "c" from "d", while "c" is less than "d"
		       			ii) increment digit in quotient
		       2. c.offset--;
		       3. 
		       4. 
		*/	

//		System.out.println("Quotient: " + quotient);
		return quotient;
	}
	
	// Creates a deep copy of the ArbNum object
	protected ArbNum copy(){
//		System.out.println("Entering copy method.");
		
		ArbNum copy = new ArbNum();
		
		for (int i = 0; i < PRECISION; i++) {
			copy.numArray[i] = this.numArray[i];
		}
		copy.offSet = this.offSet;
		copy.sign = this.sign;
		
		return copy;
	}

	//multiplies by 10 through addition; moves without changing decimal point position
	protected void mul10(){
//		System.out.println("Entering mul10 method.");
		// same as shiftRight; leaves out offset++
		for(int i = PRECISION -1;i>0;i--) numArray[i] = numArray[i-1];
	
		numArray[0] = 0;
	}
	
	//divides by 10 through addition; moves without changing decimal point position
	protected void div10(){
//		System.out.println("Entering div10 method.");
		// same as shiftRight; leaves out offset--
		for(int i = 0;i<PRECISION-1;i++) numArray[i] = numArray[i+1];

		numArray[PRECISION-1] = 0;	
	}
	
	//create shift and sign methods
	
	protected void shiftLeft(){
//		System.out.println("Entering shiftleft");
		mul10();
		offSet++;
	}	
	
	protected void shiftRight(){
//		System.out.println("Entering shiftright");
		div10();
		offSet--;
	}	
	
	protected boolean isGreaterOrEqual(ArbNum that){
//	System.out.println("Entering isGreaterOrEqual");
		if(this.offSet != that.offSet){
			return this.offSet < that.offSet;
		}
		int i=PRECISION - 1;
		while(this.numArray[i]==that.numArray[i] && i>0) i--;
		return this.numArray[i]>=that.numArray[i];
	}
	
	protected boolean equals(ArbNum that){
//	System.out.println("Entering isGreaterOrEqual");
		if(this.sign!=that.sign){
			return false;
		}if(this.offSet!=that.offSet){
			return false;
		}
		for(int i=0; i<PRECISION; i++){
			if(this.numArray[i] != that.numArray[i])
				return false;
		}
		return true;
	}
	
	protected boolean isWholeNum(){
		for(int i=0;i<this.offSet;i++){
			if(this.numArray[i]!=0)
				return false;
		}
		return true;
	}
	
	protected boolean isEven(){
		int onesPlace = this.numArray[this.offSet];
		System.out.println("onesPlace: " + onesPlace);
		return onesPlace!=1 && onesPlace!=3 && onesPlace!=5 && onesPlace!=7 && onesPlace!=9;
	}
	
	//A string representing the ArbNum
	public String toString(){
		String str="";
		String pn="";
		if(sign) pn="-";
		
		for(int i=1; i <= PRECISION; i++){
			if(offSet==PRECISION-i + 1) str+=".";
			str+=numArray[PRECISION-i]+" ";
		}
		return "Array: " + pn + str + "("+offSet +")";
	}
	
}
