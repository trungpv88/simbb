package sim;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

public final class Sim {
	private double _height;

	Sim(double height) {
		_height = height;
	}

	public Sim(byte[] data) throws java.io.IOException {
		fromByteArray(data);
	}

	public double getHeight() {
		return _height;
	}

	public void setHeight(double _height) {
		this._height = _height;
	}

	byte[] toByteArray() throws java.io.IOException {
		byte[] data;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeDouble(_height);
		data = baos.toByteArray();

		return data;
	}

	/**
	 * Converts a byte array to a CD
	 * 
	 * @param array
	 *            CD encoded as a byte array
	 * @exception java.io.IOException
	 *                Thrown if an I/O exception error occurs
	 */
	private void fromByteArray(byte[] array) throws java.io.IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(array);
		DataInputStream dis = new DataInputStream(bais);

		//short tag;

		try {
			while (true) {
				System.out.println("frombyte");
				//tag = dis.readShort();
				_height = dis.readInt();
			}
		} catch (EOFException e) {
			System.out.println("frombyteerrrrrrrrrrrrr");
		}
	}

}
