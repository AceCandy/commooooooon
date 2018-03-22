import com.br.common.util.BrCipherMaker;


public class TestChiper {

	public static void main(String[] args) {
		String cell = "18656992610";
		String encode = BrCipherMaker.getInstance().encode(cell);
		String decode = BrCipherMaker.getInstance().decode(encode);
		System.out.println(encode);
		System.out.println(decode);
		
	}
}
