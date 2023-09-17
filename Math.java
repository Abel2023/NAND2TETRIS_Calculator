// This file does all the computation for the calculator by using limit logic. Some of the functions calculated here include sine, cosine, tangent, root, and complex functions (natural/log, ln, e, pi).
public class Math{
	public final ArbNum pi;
	public final ArbNum e;
	public final ArbNum delta;
	public final ArbNum[] c;
	
	
	public Math(){
		pi = new ArbNum("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
		e = new ArbNum("2.71828182845904502874713526624977572470936999595749669676277240766303535475945713821785251664274274663");
		delta = new ArbNum("0.000,000,000,000,1");
		c = new ArbNum[3];
		c[0] = new ArbNum();
		c[1] = new ArbNum("1.0");
		c[2] = new ArbNum("2.0");
	}
	
	public ArbNum pow(ArbNum x, ArbNum y){
		if(x.equals(c[0]))
			return c[0];
		if(y.equals(c[0]))
			return c[1];
		if(x.equals(c[1]))
			return c[1];
		if(y.equals(c[1]))
			return x;
		if(x.sign){
			x.sign = false;
			if(!y.isWholeNum()){
				System.out.println("ERROR, CANT FIND POWER OF -X WITH DECIMAL Y");
				return null;
			}
			if(y.isEven()){
				System.out.println("Y is even");
				return pow(x,y);
			}
			System.out.println("Y is not even");
			ArbNum newResult = pow(x,y);
			newResult.sign = true;
			return newResult;
		}
		if(y.sign){
			y.sign = false;
			return c[1].div(pow(x,y));
		}
		
		ArbNum result = c[1].copy();
		
		while(y.isGreaterOrEqual(c[1])){
			result = result.mul(x);
			y = y.sub(c[1]);
		}
		
		return result.mul(exp( y.mul( ln(x) ) ));
	}

	public ArbNum exp(ArbNum x){
		// Takes e^x, and returns e^x
		// using taylor series 
		if(x.equals(c[0])) return c[1];
		if(x.equals(c[1])) return e;
		ArbNum sum = c[1].add(x);
		ArbNum temp = x.copy();
		ArbNum I = c[1].copy();

		while(temp.isGreaterOrEqual(delta)){
			I = I.add(c[1]);
			temp = temp.mul( x.div(I) );	
			System.out.println(sum);
			sum = sum.add(temp);
		}
		return sum;
	}
	
	public ArbNum ln(ArbNum x){
		//polynomial series

		if(x.equals(c[1])){
			return c[0];
		}
		
		if(x.equals(e)){
			return c[0];
		}

		ArbNum L =  x.sub( c[1] ).div( x.add( c[1] ) );
		ArbNum p = c[1].copy();
		ArbNum square = L.mul(L);
		ArbNum term;
		ArbNum sum = L.copy();
		
		// From here afterwards, this takes care of n=1, and future
		do{			
			p=p.add(c[2]);
			System.out.println("P: " + p);
			L = L.mul(square);
			term = L.div(p);
			
			sum = sum.add(term);
			System.out.println("Term: " + term);
			System.out.println("Sum: " + sum);

			
		
		}while(term.isGreaterOrEqual(delta));
		
		return sum.mul(c[2]);
	}
	
	public ArbNum root(ArbNum x, ArbNum y){

		
		return pow(x, c[1].div(y));
	}
	
	// MONDAY, FINISH THESE 3 FUNCTIONS
	

	public ArbNum sin(ArbNum x){
		return cos(pi.div(c[2]).sub(x));
	}
	
	public ArbNum cos(ArbNum x){

		 if(x.equals(c[0])) return c[1];
		 
		 ArbNum pi2 = pi.mul(c[2]);
		 pi2.sign = x.sign;
		 
		 System.out.println("PI2: " + pi2);
		 while(x.isGreaterOrEqual(pi2)){
		 	System.out.println("X: " + x);
			x = x.sub(pi2);
		 }
		 
		 System.out.println("NEW X: " + x);
     // create -x^2 constant
     // create int to increase by 2 in loop: 1*2, 3*4, 5*6
     // create term/L
     
     	ArbNum consty = x.mul(x);
     	consty.sign = true;
     	
     	System.out.println("SQUARE: " + consty);
     	
     	ArbNum factorialNum = c[1].copy();
     	
     	ArbNum term = c[1].copy();
     	
     	ArbNum inter;
     	
		ArbNum result = c[1].copy();

		do{
			inter = consty.div(factorialNum);
			factorialNum = factorialNum.add(c[1]);
			inter = inter.div(factorialNum);
			factorialNum = factorialNum.add(c[1]);
			term = term.mul(inter);
			result = result.add(term);
			
			System.out.println("Inter: " + inter);
			System.out.println("FactorialNum: " + factorialNum);
			System.out.println("Term: " + term);
			System.out.println("Result:" + result + "\n");

		}while(term.isGreaterOrEqual(delta));
		
		return result;
	}
	
	public ArbNum tan(ArbNum x){
		return sin(x).div(cos(x));
	}

	

}
