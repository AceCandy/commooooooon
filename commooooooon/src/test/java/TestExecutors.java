import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import com.br.common.util.BrCipherMaker;

public class TestExecutors {
	public static AtomicLong totalExecCount = new AtomicLong(0L);
	static ExecutorService pools = Executors.newFixedThreadPool(40);
//	static ThreadPoolExecutor pools = BrExecutors.getThreadPool(2, 15);
	 
	public static void main(String[] args) {
		for (int i = 0; i < 500; i++) {
			
			pools.submit(new Task());
			pools.submit(new Task2());
			System.out.println(pools.toString());
		}
		System.out.println("添加完毕");
	}
	
	private static String s = "01234含哦了發的嗯嗯啊啊dd匹配一二三五吧56~!@#$\\d%^&*()789abc defghijk66^$%lmnopqrs tuvwxyzABCDEFGHIJKLMNOPQRS&#T@UVWX.?YZ.";
	public static char[] endFix = s.toCharArray();
	
	static class Task implements Runnable{

		@Override
		public void run() {
			try {
				for (int i = 0; i < 10000; i++) {
					String cell = new Random().nextLong()+"abcxXA"+new Random().nextLong();
//					System.out.println(cell);
					String m = BrCipherMaker.getInstance().encode(cell);
					String mm = BrCipherMaker.getInstance().decode(m);
					
					if(!StringUtils.equals(cell, mm)){
						System.out.println("-----------------------------异常" + cell + "  " +mm);
					}else{
//						System.out.println("正常" + cell + "  " +m);
					}
				}
				System.out.println("完毕--------");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	static class Task2 implements Runnable{

		@Override
		public void run() {
			try {
				for (int i = 0; i < 100000; i++) {
					String cell = new Random().nextLong()+"abcxXA"+endFix[new Random().nextInt(endFix.length)];
					for (int j = 0; j < new Random().nextInt(30); j++) {
						cell += endFix[new Random().nextInt(endFix.length)];
					}
					
//					System.out.println(cell);
					String m = BrCipherMaker.getInstance().encode(cell);
					String mm = BrCipherMaker.getInstance().decode(m);
					
					if(!StringUtils.equals(cell, mm)){
						System.out.println("-----------------------------异常" + cell + "  " +mm);
					}else{
//						System.out.println("正常" + cell + "  " +m);
					}
				}
				System.out.println("完毕--------");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}

