import com.tianshouzhi.rpc.processor.RpcProcessor;
import com.tianshouzhi.rpc.RpcServer;

public class RpcServerTest {
	public static void main(String[] args) throws Exception {
		RpcServer rpcServer = new RpcServer("127.0.0.1", 8080, new RpcProcessor() {
			public Object process(Object request) {
				if (request instanceof String) {
					return "echo:" + request;
				}
				return null;
			}
		});
		rpcServer.run();
	}
}
