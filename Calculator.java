// This file creates a Calculator project, which is used alongside Math.java to run the calculator. It can be considered the interface, as it reads key words & inputs for certain operations, as well as uses an array to simulate certain functions.
import java.io.IOException;

public class Calculator{
	
	private static ArbNum[] stack;
	private static int head; 
	
	public Calculator(){
	
		stack = new ArbNum[10];
		head = 0;

	}
	
	public ArbNum pop() {
	    --head;
		return stack[head];
	}
	
	
	public void push(ArbNum value){
		stack[head] = value;
		head++;
	}
	
	public static void main(String[] args) throws IOException{
		Calculator c = new Calculator();
		Math m = new Math();
		int ch;
		ArbNum one, two;
		String num = "";

	    while (true) {
	    	ch = System.in.read();
	    	switch(ch) {
	    		case '+':
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(two.add(one));
	    			break;
	    		case '-':
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(two.sub(one));
	    			break;
	    		case '*':
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(two.mul(one));
	    			break;
	    		case '/':
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(two.div(one));
	    			break;	
	    		case 's':  // sin
	    			one = c.pop();
	    			c.push(m.sin(one));
	    			break;
	    		case 'c':  // cos
	    			one = c.pop();
	    			c.push(m.cos(one));
	    			break;	
	    		case 't':  // tan
	    			one = c.pop();
	    			c.push(m.tan(one));
	    			break;
	    		case '^':  // power^2
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(m.pow(two,one));
	    			break;
	    		case 'r':  // root
	    			one = c.pop();
	    			two = c.pop();
	    			c.push(m.root(two,one));
	    			break;
	    		case '0':
	    		case '1':
	    		case '2':
	    		case '3':    		
	    		case '4':
	    		case '5':	    	 	
	    		case '6':
	    		case '7':   	 	
	    		case '8':   	 	
	    		case '9':
	    	    case '.':  // decimal point
	    			num += (char)ch;
	    			break;  	 	
	    		case 'e':  // 2.7182818
	    			c.push(m.e);
	    	 		break;
	    	 	case 'p':  // pi (3.1415926)
	    	 	    c.push(m.pi);
	    	 		break;
	    	 	case 'R':  // return
					c.push(new ArbNum(num));
	    	 	case 'C':  // clear current number
	    	 		num = "0";
	    	 		break;
	    	 	
	    		case 10:
	    			break;  
	    	 	default:  // unknown chacter entered/used
	    	 		System.out.println("Unknown ch: " + ch);
	    	}
	    	
	    	for(int i=0;i<head;i++){
	    		System.out.println(stack[i]);
	    	}
	    	
	    	// prints out num
	    	System.out.println("\n"+num+"\n");
	    }
	}
}
