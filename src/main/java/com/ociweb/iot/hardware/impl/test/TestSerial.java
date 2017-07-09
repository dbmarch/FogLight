package com.ociweb.iot.hardware.impl.test;


import com.ociweb.pronghorn.iot.rs232.RS232Clientable;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.RawDataSchema;

public class TestSerial implements RS232Clientable {

	private final Pipe<RawDataSchema> pipe;
	private DataInputBlobReader<RawDataSchema> fieldByteArray;
	
	public TestSerial() {
		
		pipe = RawDataSchema.instance.newPipe(100, 500);
		pipe.initBuffers();
		
	}
	
	@Override
	public int readInto(byte[] array, int position, int remaining, byte[] array2, int position2, int remaining2) {
		
		int consumed = 0;
		
		if (PipeReader.tryReadFragment(pipe)) {
		    int msgIdx = PipeReader.getMsgIdx(pipe);
		    
		    
		    switch(msgIdx) {
		        case RawDataSchema.MSG_CHUNKEDSTREAM_1:
		        	
		        	if (null == fieldByteArray) {		        	
		        		fieldByteArray = PipeReader.inputStream(pipe, RawDataSchema.MSG_CHUNKEDSTREAM_1_FIELD_BYTEARRAY_2);
		        	} else {
		        		fieldByteArray.accumHighLevelAPIField(RawDataSchema.MSG_CHUNKEDSTREAM_1_FIELD_BYTEARRAY_2);
		        	}
		        			        	
		        	int avail = fieldByteArray.available();
		        	if (avail <= remaining) {
		        		consumed += fieldByteArray.read(array, position, avail);
		        		//System.err.println(array[position]+" at pos "+position);
		        		avail = 0;
		        	} else {
		        		consumed += fieldByteArray.read(array, position, remaining);
		        		avail-=remaining;		        		
		        		if (avail<=remaining2) {
		        			consumed += fieldByteArray.read(array2, position2, avail);
		        			avail = 0;
		        		} else {
		        			consumed += fieldByteArray.read(array2, position2, remaining2);		        			
		        			avail-=remaining2;
		        			//we still have avail left over		        			
		        		}
		        	}
		        break;
		    }
		    		    
		    PipeReader.readNextWithoutReleasingReadLock(pipe);
		    PipeReader.releaseAllPendingReadLock(pipe, consumed);

		}
		return consumed;
	}

	@Override
	public int writeFrom(byte[] backing, int pos, int length) {
		RawDataSchema.publishChunkedStream(pipe, backing, pos, length);
		return length; //must be length to indicate all consumed
	}

	@Override
	public int write(byte[] data) {		
		RawDataSchema.publishChunkedStream(pipe, data, 0, data.length);
		return data.length;
	}

}
