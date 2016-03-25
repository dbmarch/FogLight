// Project: PronghornIoT
// Since: Feb 21, 2016
//
///////////////////////////////////////////////////////////////////////////////
/**
 * TODO: Which license?
 */
///////////////////////////////////////////////////////////////////////////////
//
package com.ociweb.device.grove.grovepi;

/**
 * Represents a generic backing for the GrovePi's I2C lines.
 *
 * TODO: This interface and all of the Grove Pi I2C code can be generified
 *       to work with any device and reduce code duplication.
 *
 * @author Brandon Sanders [brandon@alicorn.io]
 */
public interface GrovePiI2CStageBacking {

    /**
     * Reads a message from the I2C device at the specified address.
     *
     * @param bufferSize Size of the byte buffer to read into (and return).
     * @param address Address of the I2C device to read, e.g., "0x32" for a
     *                GrovePi's LCD RGB backlight.
     * @param message Array of bytes to write to the I2C device.
     *
     * @return Data received from the I2C device, or an empty array if no
     *         data was received.
     */
    byte[] read(int bufferSize, byte address, byte... message);

    /**
     * Writes a message to an I2C device at the specified address.
     *
     * @param address Address of the I2C device to write, e.g., "0x32" for a
     *                GrovePi's LCD RGB backlight.
     * @param message Array of bytes to write to the I2C device.
     */
    void write(byte address, byte... message);
}
