package com.tianshouzhi.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by tianshouzhi on 2018/6/30.
 */
public class ByteBufTest {
	@Test
	public void testHeapOrDirect() {
		// 创建heaper buffer
		ByteBuf heapBuffer = Unpooled.buffer(16, 32);
		System.out.println(heapBuffer);
		System.out.println(heapBuffer.unwrap());
		boolean direct = heapBuffer.isDirect();
		if (heapBuffer.hasArray()) {// 判断底层是基于字节数组，才可以调用array()和arrayOffset()方法
			byte[] array = heapBuffer.array();
			int arrayOffset = heapBuffer.arrayOffset();
			// do some thing
		}

		// 创建direct buffer
		ByteBuf directBuffer = Unpooled.directBuffer(16, 32);
		direct = directBuffer.isDirect();
		if (directBuffer.hasMemoryAddress()) {// 判断是具有内存地址，才可以调用memoryAddress()方法
			long memoryAddress = directBuffer.memoryAddress();
			// do some thing
		}
	}

	@Test
	public void testBasicAPI() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		printBuffer(buffer);
	}

	private void printBuffer(ByteBuf buffer) {
		System.out.println("readerIndex:" + buffer.readerIndex());// 当前readerIndex索引
		System.out.println("writerIndex:" + buffer.writerIndex());// 当前writerIndex索引
		System.out.println("capacity:" + buffer.capacity());// 当前capacity索引
		System.out.println("maxCapacity:" + buffer.maxCapacity());// 最大capacity
		System.out.println("readableBytes:" + buffer.readableBytes());// 可读字节数，值为writerIndex-readerIndex
		System.out.println("isReadable:" + buffer.isReadable());// 是否可读，writerIndex-readerIndex>0时，返回true，否则为false
		System.out.println("writableBytes:" + buffer.writableBytes());// 可写字节数，值为capacity-writerIndex
		System.out.println("isWritable:" + buffer.isWritable());// 是否可写，当capacity-writerIndex>0时，为true
	}

	@Test
	public void testSequenceWrite() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
		printBuffer(buffer);
	}

	@Test
	public void testSequenceRead() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
		CharSequence charSequence = buffer.readCharSequence(5, CharsetUtil.ISO_8859_1);
		System.out.println("读取内容：" + charSequence);
		printBuffer(buffer);
	}

	@Test
	public void testDiscardReadBytes() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
		CharSequence charSequence = buffer.readCharSequence(5, CharsetUtil.ISO_8859_1);
		buffer.discardReadBytes();
		printBuffer(buffer);
	}

	@Test
	public void testClear() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
		CharSequence charSequence = buffer.readCharSequence(5, CharsetUtil.ISO_8859_1);
		buffer.discardReadBytes();
		buffer.clear();// 清空数据
		printBuffer(buffer);
	}

	@Test
	public void testIndexOfAndBytesBefore() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);

		int indexOf = buffer.indexOf(buffer.readerIndex(), buffer.writerIndex(), (byte) 'N');
		System.out.println(indexOf);// 打印6

		int bytesBefore = buffer.bytesBefore((byte) 'N');

		assert indexOf == bytesBefore;
	}

	@Test
	public void testByteProcessor() {
		ByteBuf buffer = Unpooled.buffer(16, 32);
		buffer.writeCharSequence("Hello,Netty", CharsetUtil.ISO_8859_1);
		int index = buffer.forEachByte(ByteProcessor.FIND_COMMA);// 查找逗号
		System.out.println(index);// 输出5
	}

	@Test
	public void testSlice() {
		Charset utf8 = Charset.forName("UTF-8");
		// 原生ByteBuf
		ByteBuf buf = Unpooled.buffer(); // 1
		buf.writeCharSequence("Netty in Action rocks!", utf8);

		// 派生ByteBuf
		ByteBuf sliced = buf.slice(0, 14);
		System.out.println(sliced.toString(utf8)); // 输出Netty in Actio

		// 修改原生ByteBuf，派生ByeBuf的内容也收到影响
		buf.setByte(0, (byte) 'J'); // 4
		assert buf.getByte(0) == sliced.getByte(0);
	}

	@Test
	public void testReferenceCount() {
		// 当一个ByteBuf被创建后，其refCnt为1
		ByteBuf byteBuf = Unpooled.buffer();
		int refCnt = byteBuf.refCnt();
		System.out.println(refCnt);// 1

		// 通过retain()方法，可以将这个ByteBuf的的refCnt加1
		byteBuf.retain();
		System.out.println(byteBuf.refCnt());// 2

		// 通过retain(int)方法，可以将这个ByteBuf的的refCnt加指定的数值
		byteBuf.retain(3);
		System.out.println(byteBuf.refCnt());// 5

		// 通过release(int)方法，可以refCnt将减少指定数量，返回值表示是否没有引用了
		boolean release = byteBuf.release(4);
		System.out.println(release);// false
		System.out.println(byteBuf.refCnt());// 1

		// 通过release()方法，可以refCnt将减少1，返回值表示是否没有引用了
		System.out.println(byteBuf.release());// true
		System.out.println(byteBuf.refCnt());// 0

		// 当byteBuf的release方法返回true，说明已经释放成功，没有引用了。不能继续被使用，否则将会抛出异常
		System.out.println(byteBuf.writeInt(1));// 抛出IllegalReferenceCountException
	}

	@Test
	public void testAllocateAndDeallocate() {

		//unpooled heap buffer
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			ByteBuf heapBuffer = UnpooledByteBufAllocator.DEFAULT.buffer();
			heapBuffer.release();
		}
		System.out.println("unpooled heap buffer:"+(System.currentTimeMillis() - start));

		//unpooled direct buffer
		 start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			ByteBuf directBuffer = UnpooledByteBufAllocator.DEFAULT.directBuffer();
			directBuffer.release();

		}
		System.out.println("unpooled direct buffer:"+(System.currentTimeMillis() - start));

		//pooled heap buffer
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			ByteBuf heapBuffer = PooledByteBufAllocator.DEFAULT.buffer();
			heapBuffer.release();
		}
		System.out.println("pooled heap buffer:"+(System.currentTimeMillis() - start));


		//pooled direct buffer
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			ByteBuf directBuffer = PooledByteBufAllocator.DEFAULT.directBuffer();
			directBuffer.release();

		}
		System.out.println("pooled direct buffer:"+(System.currentTimeMillis() - start));
	}

	@Test
	public void testUnsafe(){

		ByteBuf nettyHeapBuffer = UnpooledByteBufAllocator.DEFAULT.buffer(256);
		ByteBuffer jdkHeapBuffer = ByteBuffer.allocate(256);

		//unsafe heap buffer
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			nettyHeapBuffer.writeLong(1234567890);
			nettyHeapBuffer.clear();
		}
		System.out.println("netty unsafe heap buffer:"+(System.currentTimeMillis() - start));


		//heap buffer
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			jdkHeapBuffer.putLong(1234567890);
			jdkHeapBuffer.clear();
		}
		System.out.println("jdk byte buffer:"+(System.currentTimeMillis() - start));
	}
}
