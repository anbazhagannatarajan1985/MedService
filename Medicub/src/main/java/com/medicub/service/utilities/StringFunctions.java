/******************* Class StringFunctions ************************************
Containd commonly used string maniplulation functions.


******************************************************************************/
package com.medicub.service.utilities;

import java.io.File;
import java.net.URLEncoder;
import java.rmi.dgc.VMID;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* String manipulation functions
*/
public class StringFunctions
{

    /*************************** Properties **********************************/




    /************************* Constants *************************************/

    private static final String SPACES = "                                                  " +
                                         "                                                  " +
                                         "                                                  " +
                                         "                                                  " +
                                         "                                                  " +
                                         "                                                  ";

    private static final String ZEROS  = "00000000000000000000000000000000000000000000000000" +
                                         "00000000000000000000000000000000000000000000000000" +
                                         "00000000000000000000000000000000000000000000000000" +
                                         "00000000000000000000000000000000000000000000000000" +
                                         "00000000000000000000000000000000000000000000000000" +
                                         "00000000000000000000000000000000000000000000000000";


    /************************** Get VMID *************************************/
    /**
     * returns unique VMID
     *
     * @return VMID as string
     */
    public static String getVMID()
    {
        return new VMID().toString();
    }

    /************************** Get UUID *************************************/
    /**
     * returns a UUID
     *
     * @return UUID as string
     */
    public static String getUUID()
    {
        //TODO Support UUID
        return new VMID().toString();
    }


    /****************************** Fill *************************************/
    /**
     * Returns a string filled with the specified character.
     *
     * @param character
     * @param length
     * @return Resultant string
     */
    public static String fill(char character, int length){

        StringBuffer buffer = new StringBuffer(length);
        for (int i=0; i<length; i++){
            buffer.append(character);
        }

        return buffer.toString();
    }



    /**************************** Truncate ***********************************/
    /**
     * Returns a string truncated to a maximum length
     *
     * @param str
     * @param length
     * @return Resultant string
     */
    public static String truncate(String str, int length){

    	if (str == null){
    		return null;
    	}
    	else if (length > 0){
        	if (str.length() > length){
        		return str.substring(0, length);
        	}
        	return str;
        }
        else {
        	return "";
        }

    }


    /************************** Find and Replace *****************************/
    /**
     * Replaces all instances of sought string in searched text
     *
     * @param searched String that is being searched
     * @param sought String to search for
     * @param replacement String to replace
     * @return Resultant string
     */
    public static String FindAndReplace(String searched,String sought,String replacement)
    {
        if (searched == null || sought == null || replacement == null) return searched;
        if (searched.length() == 0 || sought.length() == 0) return searched;

        int startPos = 0;
        int nextPos = searched.indexOf(sought);

        if (nextPos == -1)
        {
            return searched;
        }

        StringBuffer result = new StringBuffer();

        while (nextPos != -1)
        {
            result = result.append(searched.substring(startPos, nextPos)).append(replacement);
            startPos = nextPos + sought.length();
            nextPos = searched.indexOf(sought,nextPos + sought.length());
        }

        return result.append(searched.substring(startPos)).toString();
    }


    /*************************** Get Random Word *****************************/
    /**
     * returns a word of the given length consisting of characters between A-Z all in capitals
     *
     * @param length length of the word required.
     * @return String equal to the random word
     */
    public static String getRandomWord(int length)
    {

        String word = "";

        for (int i=0;i<length;i++)
        {

            word = word + String.valueOf((char)(int)(Math.random() * 25 + 65));

        }
        return word;

    }

    public static String generateStrongPassword(int length) {
    	
    	Random rand = new Random(System.currentTimeMillis());
    	
    	StringBuffer characters = new StringBuffer(getRandomWord(8));
    	
    	// mix case
    	for (int i=0; i<characters.length(); i++) {
    		
    		if (rand.nextInt(2)==1) {
    			characters.setCharAt(i, Character.toLowerCase(characters.charAt(i)));
    		}
    	}
    	
    	int number = rand.nextInt(9999);
    	
    	return characters.append(padLeftZeros(String.valueOf(number), 4)).toString();
    }

    /********************** Split String *************************************/
    /**
     * Splits a string into a string array given a separator.
     *
     * @param splitWord word to split
     * @param separator separator to split by.
     * @return String array of split words.
     */
    public static String[] splitString(String splitWord,String separator)
    {

        int count = 1;
        int startPos = 0;
        String[] returnStrings = null;

        if (separator.length()!=0)
        {
            //firstly find out how many words are in the string
            int nextPos = splitWord.indexOf(separator,startPos);

            while (nextPos != -1)
            {
                startPos = nextPos + separator.length();
                nextPos = splitWord.indexOf(separator,startPos);

                count++;
            }

            //dimension the string array to fit all of the words
            returnStrings = new String[count];

            startPos = 0;
            nextPos = splitWord.indexOf(separator,startPos);
            count = 0;

            //loop again filling the array
            while (nextPos != -1)
            {

                returnStrings[count] = splitWord.substring(startPos,nextPos);

                startPos = nextPos + separator.length();
                nextPos = splitWord.indexOf(separator,startPos);

                count++;
            }

            //add the final string to the array
            returnStrings[count] = splitWord.substring(startPos);

        }else{
            //if no separator just return the string
            returnStrings = new String[count];
            returnStrings[0] = splitWord.substring(startPos);
        }

        return returnStrings;

    }


    /********************** Split String *************************************/
    /**
     * Splits a string into an array of words. Delimiters assumed are space,
     * carriage return character and new line character.
     * Note that words may be separated by more than one space.
     *
     * @param splitWord word to split
     * @return string[] collection of split words.
     */
    public static String[] splitString(String splitWord)
    {
        Vector v = new Vector();

        int startOfWord = -1;
        int endOfWord = -1;
        boolean inWord = false;
        int i = 0;

        for (i = 0; i < splitWord.length(); i++) {
            // Read the next character in the string
            char c = splitWord.charAt(i);
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                // This is a delimiter - if inWord is set then this marks the end of the current word so add it to the list of words
                if (inWord) {
                    endOfWord = i;
                    v.add(splitWord.substring(startOfWord, endOfWord));
                    inWord = false;
                }
            } else {
                // This is not a delimiter - if inWord is not set then this marks the start of a new word
                if (!inWord) {
                    startOfWord = i;
                    inWord = true;
                }
            }
        }

        // We may not have got the last word yet
        if (inWord) {
            endOfWord = i;
            v.add(splitWord.substring(startOfWord, endOfWord));
        }

        return (String[])v.toArray(new String[v.size()]);
    }


	/********************** Split String *************************************/
	/**
	 * Splits a string into an array of words, split at specified delimiter,
	 * and handling quoted strings. Intended for processing comma-delimited data.
	 *
	 * @param delimiter delimiter char - normally ','
	 * @param quoteCharacter the character to that acts to quote a string,
	 * normally '\"', zero if not required.
	 *
	 * @param line line to split
	 * @return string[] collection of split words.
	 *
	 * Note that you get a string for every delimited word, and so may get empty
	 * strings, and you will alwways get at least one string if line is not null.
	 */
	public static String[] splitString(String line, char delimiter, char quoteCharacter)
	{
		Vector v = new Vector();

		if (line != null)
		{
			boolean inQuotes = false;
			String word = "";
			for (int i = 0; i < line.length(); i++) {
				// Read the next character in the string
				char c = line.charAt(i);

				if (quoteCharacter != 0 && c == quoteCharacter) {
					inQuotes = !inQuotes;
				} else {
					if (!inQuotes && c == delimiter) {
						// This is a delimiter - so add current word to the list of words
						v.add(word);
						word = "";
					} else {
						word += c;
					}
				}
			}
			// Always add the last word
			v.add(word);
		}

		return (String[])v.toArray(new String[v.size()]);
	}





    /************************** Get Word *************************************/
    /**
    * Get the nth word in a list separated by a separator.
    */
    public static String getWord(String words,String separator,int index)
    {
        int startPos = 0;
        int nextPos = 0;
        int count = 1;

        //if any of the input strings are null then return null
        if (words==null || separator==null) return null;

        nextPos = words.indexOf(separator);

        //loop to find the start and end of the specified word
        while(nextPos!=-1 && count<index)
        {
            startPos = nextPos + separator.length();
            nextPos = words.indexOf(separator,startPos);
            count++;
        }

        //if the word was found then return it
        if (nextPos!=-1)
            return words.substring(startPos, nextPos);
        else if (count==index)
            return words.substring(startPos);
        else
            return null;
    }


    /************************** Get Word *************************************/
    /**
    * Get the nth word in a list separated by a start separator and an end
    * separator.
    */
    public static String getWord(String words, String startSeparator, String endSeparator, int index)
    {

        int startPos = -1;
        int nextPos = -1;
        int endPos = -1;
        int count = 0;

        //if any of the input strings are null then return null
        if (words==null || startSeparator==null || endSeparator==null) return null;

        nextPos = words.indexOf(startSeparator);

        //loop to find the start and end of the specified word
        while(nextPos!=-1 && count<index)
        {
            startPos = nextPos + startSeparator.length();
            endPos = words.indexOf(endSeparator,startPos);
            nextPos = words.indexOf(startSeparator,endPos);
            count++;
        }

        //if the word was found then return it
        if ((startPos != -1) && (endPos != -1))
            return words.substring(startPos, endPos);
        else
            return null;

    }

    /************************** Get Word *************************************/
    /**
    * Get the nth word in a list separated by a start separator and an end
    * separator.
    */
    public static String[] getWords(String words, String startSeparator, String endSeparator)
    {

        //find the count of the start separator

        int count = 0;
        int index = 0;

        while ((index = words.indexOf(startSeparator, index))!= -1){
            count++;
            index++;
        }

        String[] strings = new String[count];

        int startPos = -1;
        int nextPos = -1;
        int endPos = -1;

        //if any of the input strings are null then return null
        if (words==null || startSeparator==null || endSeparator==null) return null;

        nextPos = words.indexOf(startSeparator);

        count = 0;

        //loop to find the start and end of the specified word
        while(nextPos!=-1)
        {
            startPos = nextPos + startSeparator.length();
            endPos = words.indexOf(endSeparator,startPos);
            nextPos = words.indexOf(startSeparator,endPos);
            //if the word was found then return it
            if ((startPos != -1) && (endPos != -1)){
                strings[count++] = words.substring(startPos, endPos);
            }
        }

        return strings;
    }

    /******************* Get Words including the separators *******************/
    /**
     * Get the words in a String[], separated by a startSeparator and an
     * endSeparator, including the separators.
     * 
     * Example: If you pass &lt;Token&gt; as startSeparator parameter,
     * &lt;/Token&gt; as endSeparator parameter and following string as words
     * parameter <br/>
     * &lt;TokenField&gt;&lt;FieldWidth&gt;19&lt;/FieldWidth&gt;&lt;Token
     * &gt;5301257069240191&lt;/Token&gt;&lt;/TokenField&gt; <br/>
     * then the return value will be String array which contains
     * {&lt;Token&gt;5301257069240191&lt;/Token&gt;}
     */
    public static String[] getWordsIncludingSeparators(String words, String startSeparator, String endSeparator)
    {
        String[] retVal = null;

        // If any of the input strings are null then return null
        if (words != null && startSeparator != null && endSeparator != null) {

            // Get the first index of startSeparator
            int nextPos = words.indexOf(startSeparator);

            if (nextPos != -1) {

                List list = new ArrayList();
                int startPos = -1;
                int endPos = -1;

                // loop to find the start and end of the specified word
                while (nextPos != -1) {

                    startPos = nextPos;
                    endPos = words.indexOf(endSeparator, startPos) + endSeparator.length();

                    // If the word was found then add it to list
                    if ((startPos != -1) && (endPos != -1)) {
                        list.add(words.substring(startPos, endPos));
                    }

                    // Get the next index of startSeparator from the endPos
                    nextPos = words.indexOf(startSeparator, endPos);
                }

                retVal = (String[]) list.toArray(new String[list.size()]);
            }
        }

        return retVal;
    }

    /************************ Pad Left **************************************/
    /**
    * Pad a string to the left with spaces to obtain a fixed width (truncate if longer)
    */
    public static String padLeft(String s, int width)
    {
        int len = s.length();
        if (len == width)
            return s;
        else if (len > width)
            return s.substring(0, width);
        else {
            String spaces = SPACES.substring(0, width-len);
            return (spaces + s);
        }

    }


    /**
    * Pad a string to the left with zeros to obtain a fixed width (truncate if longer)
    */
    public static String padLeftZeros(String s, int width)
    {
        return padLeftZeros(s, width, false);
    }

    /**
     * Pad a string to the left with zeros to obtain a fixed width (truncate if longer)
     * If the afterMinus flag is true and the passed in string begins with a minus symbol,
     * the zeros will be added after that symbol. Note that the minue symbol still constitutes
     * part of the overall width.
     *
     * @param s
     * @param width
     * @param afterMinus
     * @return
     */
    public static String padLeftZeros(String s, int width, boolean afterMinus)
    {
        int len = s.length();
        if (len == width)
            return s;
        else if (len > width)
            return s.substring(0, width);
        else
        {
            String zeros = ZEROS.substring(0, width-len);
            if (afterMinus && s.charAt(0) == '-')
                return '-' + zeros + s.substring(1);
            else
                return (zeros + s);
        }

    }



    /************************* Pad Right *************************************/
    /**
    * Pad a string to the right with spaces to obtain a fixed width (truncate if longer)
    */
    public static String padRight(String s, int width)
    {
        int len = s.length();
        if (len == width)
            return s;
        else if (len > width)
            return s.substring(0, width);
        else {
            String spaces = SPACES.substring(0, width-len);
            return (s + spaces);
        }
    }

    /**
    * Pad a string to the right with zeros to obtain a fixed width (truncate if longer)
    */
    public static String padRightZeros(String s, int width)
    {
        int len = s.length();
        if (len == width)
            return s;
        else if (len > width)
            return s.substring(0, width);
        else {
            String zeros = ZEROS.substring(0, width-len);
            return (s + zeros);
        }
    }

    /**
     *  Strip leading zeros
     */
    public static String stripLeadingZeros(String value)
    {

        //if the value is null then return
        if (value==null) return null;

        StringBuffer sb = new StringBuffer(value);

        //loop over the leading zeros removing them
        while (sb.length()>0 && sb.charAt(0)=='0')
        {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }




    /*************************** Center **************************************/
    /**
    * Center a string with spaces to obtain a fixed width. Truncate if longer
    * preserving from the left.
    */
    public static String center(String s, int width)
    {
        int len = s.length();
        if (len == width)
            return s;
        else if (len > width)
            return s.substring(0, width);
        else {
            String leftSpaces = SPACES.substring(0, (width-len)/2);
            String rightSpaces = SPACES.substring(0, width-len-leftSpaces.length());
            return (leftSpaces + s + rightSpaces);
        }

    }


    /********************* Convert to Hex ************************************/
    /**
    * Convert a byte to characters in hex format
    */
    public static String byteToHex(byte value){

        int topBits = ((int)value & 0xF0)>>>4;           //>>> shifts in zeros at the top
        int bottomBits = (value & 0x0F);
        int topChar = (topBits < 10) ? ('0' + topBits) : ('A' + topBits - 10);
        int bottomChar = (bottomBits < 10) ? ('0' + bottomBits) : ('A' + bottomBits - 10);

        return (String.valueOf((char)topChar) + String.valueOf((char)bottomChar));

    }



    /********************* Convert from Hex ***********************************/
    /**
    * Convert a string in hex format to a byte
    */
    public static byte hexToByte(String hex){

        char topChar = hex.charAt(0);
        char bottomChar = hex.charAt(1);
        int topBits = ((topChar >= '0') && (topChar <= '9')) ? (topChar - '0') : (topChar - 'A') + 10;
        int bottomBits = ((bottomChar >= '0') && (bottomChar <= '9')) ? (bottomChar - '0') : (bottomChar - 'A') +10;
        topBits <<= 4;       //Shifts in zeros

        return ((byte)(topBits | bottomBits));

    }


    /********************* Convert to Hex ************************************/
    /**
    * Convert a byte to characters in hex format
    */
    public static String shortToHex(short value){

        return padLeftZeros(Integer.toHexString(value), 4);

    }

    /********************* Convert from Hex ***********************************/
    /**
    * Convert a string in hex format to a byte
    */
    public static short hexToShort(String hex){

        try {
            int value = Integer.parseInt(hex, 16);
            return (short)value;
        }catch(NumberFormatException nfe){
            return 0;
        }

    }

	/*********************** Hex String to Byte Array ************************/
	/**
	 * Convert a String of ascii Hex digits (commonly known as "Ascii
	 * Representation of Hex") into a byte array.
	 * The length of the output byte array will be half the string length.
	 *
	 * e.g. the String FF005B23000100 would be converted into an array
	 * [0xFF, 0x00, 0x5B, 0x23, 0x00, 0x01, 0x00]
	 *
	 * @param string the hex string to convert
	 * @return corresponding byte array
	 */
	public static byte[] hexStringToByteArray(String s)
	{
		byte[] bytes = null;
		if (s != null) {
			int numBytes = s.length() / 2;
			bytes = new byte[numBytes];
			for (int i = 0, offset = 0; i < numBytes; i++, offset += 2) {
				bytes[i] = StringFunctions.hexToByte(s.substring(offset, offset + 2));
			}
		}
		return bytes;
	}


	/*********************** Byte Array to Hex String ************************/
	/**
	 * Convert a byte array into an ascii hex String (commonly known as "Ascii
	 * Representation of Hex").
	 * The resulting string will be twice the length of the byte array.
	 *
	 * e.g the byte array [0xFF, 0x00, 0x5B, 0x23, 0x00, 0x01, 0x00]
	 * would be converted into the String FF005B23000100
	 *
	 * @param bytes the byte array
	 * @return converted string or "" if data invalid
	 */
	public static String byteArrayToHexString(byte[] bytes)
	{
		return byteArrayToHexString(bytes, 0, bytes != null ? bytes.length : 0);
	}

	/**
	 * Convert a part of a byte array into an ascii hex String (commonly known
	 * as "Ascii Representation of Hex").
	 * The resulting string will be twice the length specified.
	 *
	 * e.g the byte array [0xFF, 0x00, 0x5B, 0x23, 0x00, 0x01, 0x00]
	 * would be converted into the String FF005B23000100
	 *
	 * @param bytes the byte array
	 * @param offset offset of start of data
	 * @length number of bytes of data to convert
	 *
	 * @return converted string or "" if data invalid
	 */
	public static String byteArrayToHexString(byte[] bytes, int offset, int length)
	{
		StringBuffer buffer = new StringBuffer();

		if (bytes != null && (offset + length) <= bytes.length) {
			for(int i = 0; i < length; i++) {
				buffer.append(StringFunctions.byteToHex(bytes[offset + i]));
			}
		}
		return buffer.toString();
	}





    /******************** String To Hex **************************************/
    /**
    * Convert a string to hex format
    */
    public static String stringToHex(String str){

        String result = "";
        byte[] bytes = toByteArray(str);
        for (int i=0; i<bytes.length; i+=2){
            result += " ";
            if (i+1 < bytes.length){
                result += byteToHex(bytes[i+1]);
            }
            result += byteToHex(bytes[i]);
        }
        return result;

    }

    /******************** Hex to ASCII **************************************/
    /**
     * Convert a hex string to ascii string
     * @param hex
     * @return
     */
	public static String hexToAscii(String hex) {
		StringBuilder output = new StringBuilder();

		for(int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	/******************************* Get PAN *********************************/
	/**
	 * Extract the PAN from card track2 data.
	 * Allows for minimal track2 data as found on some loyalty-type cards
	 * and does not require access to IIN range data.
	 *
	 * @param track2 track2 data from card magnetic stripe
	 * @return PAN if found, null if cannot get PAN
	 */
	public static String getPAN(String track2)
	{
		String pan = null;

		// Mimimum requirement is Track2 ALWAYS starts with ';' start sentinel
		if (track2 != null && track2.length() > 1 && track2.charAt(0) == ';')
		{
			// First try standard credit card style with '=' separator present
			int end;
			if ((end = track2.indexOf('=')) != -1) {
				// BZ56395: '=' could be an LRC
				if (hasLRC(track2) && end == track2.length() - 1) {
					if ((end = track2.indexOf('?')) != -1)
						pan = track2.substring(1, end);
				}
				else
					pan = track2.substring(1, end);
			}
			// Otherwise assume just '?' end sentinel present
			else if ((end = track2.indexOf('?')) != -1)
				pan = track2.substring(1, end);
		}

		return pan;
	}

	/**
	 * Returns true if the track2 data has an LRC.
	 * @param track2
	 * @return hasLRC
	 */
	public static boolean hasLRC(String track2)
	{
		return track2 != null && track2.length() > 2 && track2.charAt(track2.length() - 2) == '?';
	}

	/**
	 * Calculates the LRC for track2 data.
	 * @param data
	 * @return LRC
	 */
	public static char calculateLRC(String data)
	{
		byte lrc = 0;
		int length = data.length();
		for (int i = 0; i < length; i++)
			lrc ^= data.charAt(i) & 0x0f;
		return (char)(lrc | 0x30);
	}
	
	/**
	 * Checks the LRC for the track2 data.
	 * @param track2
	 * @return boolean
	 */
	public static boolean checkLRC(String track2)
	{
		if (hasLRC(track2))
			return track2.charAt(track2.length() - 1) == calculateLRC(track2.substring(0, track2.length() - 1));
		else
			return false;
	}
	
    /************************ Format PAN *************************************/
    /**
    * Format a PAN number using a string that supplies the embossed digit
    * groupings. For example: "4,4,4,0,0,0" would format "567878764543" as
    * "5678 7876 4543"
    */
    public static String formatPAN(String pan, String embossedDigitGroupings){

        String result= "";
        int digits;
        int pos = 0;
        String[] groupings = StringFunctions.splitString(embossedDigitGroupings, ",");

        //Go through each grouping and pull out the digits
        for (int i = 0; i < groupings.length; i++){

            try {
                digits = Integer.parseInt(groupings[i]);
                if (digits < 1)
                    continue;
            } catch (NumberFormatException e){
                continue;
            }

            //If not enough characters left in the PAN then use what we have
            if (pos+digits > pan.length()) {
                if (pos != 0)
                    result += ' ' + pan.substring(pos, pan.length());
                else
                    result += pan.substring(pos, pan.length());
                pos = pan.length();
                break;
            }
            else {
                if (pos != 0)
                    result += ' ' + pan.substring(pos, pos+digits);
                else
                    result += pan.substring(pos, pos+digits);
                pos += digits;
            }

        }//for

        //Any PAN characters left over just add to the end
        if (pan.length() > pos) {
            if (pos != 0)
                result += ' ' + pan.substring(pos, pan.length());
            else
                result += pan.substring(pos, pan.length());
        }


        return result;

    }

    /**
     * This routine mangles the password from the connection string (if other forms are used 
     * these need to be added or parameterised
     * 
     * @param connectionString
     * @return
     */
    public static String mangleConnectionString(String connectionString) {
    	StringBuffer output = new StringBuffer(connectionString);
    	char[] terminationChars = { ',', ';' };
    	String[] passwordStr = new String[] { "password=", "pwd=" };
    	
		if (StringFunctions.isNotEmpty(connectionString)) {
			String buf = connectionString.toLowerCase();
			for (int i=0; i<passwordStr.length; i++) {
    			for (int startIndex = 0, index = 0; (index=buf.indexOf(passwordStr[i],startIndex))!=-1; startIndex=index) {
    				index+= passwordStr[i].length();
    				for (int j=index; !testTerminationChars(buf, j, terminationChars); j++) {
    					output.setCharAt(j, '*');
    				}
    			}
    		}
			connectionString = output.toString();
    	}
    	return connectionString;
    }
    
    /**
     * check for preset terminators
     * 
     * @param buf
     * @param i
     * @param terminationChars
     * @return
     */
    private static boolean testTerminationChars(String buf, int i, char[] terminationChars) {
    	
    	boolean found = i>=buf.length();
    	if (!found) {
    		for (int j=0; j<terminationChars.length && !found; j++) {
    			 found = (buf.charAt(i) == terminationChars[j]);
    		}
    	} 
    	return found;
    }
    
//    /************************ Mangle Track Data *************************************/
//
//    /**
//     * Based on a configuration debug setting (NoMangleTrackData), either returns the string intact
//     * or an empty string.
//     */
//    public static String mangleTrackData(String trackData)
//    {
//        if (ConfigServer.testClassForDebug("NoMangleTrackData")) {
//        	Logger.writeLog("StringFunctions.mangleTrackData()", "The skip mangle function has been disabled for PA-DSS accreditation. This function may not be used in a PA-DSS compliant release.", Logger.LOG_INFORMATION);
//            if (isNotEmpty(trackData)) {
//            	// replace any numbers with * - this will maintain the correct length & sentinels 
//            	return trackData.replaceAll("\\d", "*");
//            }
//        }
//        return "";
//    }
    
    /**
     * apply a mask to mangling the PAN. If the mask or pan is not valid
     * then apply the standard mangling
     * 
     * @param pan
     * @param mask
     * @return
     */
    public static String manglePAN(String pan, String mask) {
    	
    	StringBuffer mangledPAN = new StringBuffer();
    	
    	if (validatePANMask(pan, mask)) {
    		for (int i=0; i<mask.length(); i++) {
    			// minimum requirement is that all characters except the first 6 & last 4 are
    			// masked
    			if (i<6 || i>=mask.length()-4) {
    				mangledPAN.append(mask.charAt(i)=='*' ? '*' : pan.charAt(i));
    			} else {
    				mangledPAN.append('*');
    			}
    		}
    	}
    
    	return mangledPAN.length()>0 || pan==null ? mangledPAN.toString() : manglePAN(pan);
    }

    /**
     * test the mask & pan are valid for mangling
     * 
     * @param pan
     * @param mask
     * @return
     */
    private static boolean validatePANMask(String pan, String mask) {
    	boolean status = pan!=null && mask!=null && pan.length()>10 && pan.length()==mask.length();
    	return status;
    }
    
    /************************ Mangle PAN *************************************/
    /**
     * Mangle a PAN number by replacing all but the last 4 digits with '*'s.
     * This appears to now be the Banks' preferred format.
     */
    public static String manglePAN(String pan)
    {
        StringBuffer mangledPan = new StringBuffer();

        int pos = pan.length();

        // Find position leaving last 4 digits intact, not counting spaces
        for (int i = 4; pos > 0 && i > 0; pos--)
        {
            if (pan.charAt(pos - 1) != ' ')
                i--;
        }

        // Copy across first part, maintaining spaces, replacing digits
        for (int i = 0; i < pos; i++)
        {
            if (pan.charAt(i) == ' ')
                mangledPan.append(' ');
            else
                mangledPan.append('*');
        }

        // And complete with the unmasked part
        mangledPan.append(pan.substring(pos, pan.length()));

        return mangledPan.toString();
    }


    /************************ Mangle PAN Centre ******************************/
    /**
     * Mangle a PAN number by inserting four '*'s in the middle
     * This is our original version.
     */
    public static String manglePANCentre(String pan)
    {
        String mangledPan = "";

        if (pan.length() < 8)
            return pan;
        else {
            int pos = (pan.length()/2) - 2;
            mangledPan = pan.substring(0, pos);
            int i=0;
            do{
                if (pan.charAt(pos) != ' '){           //Don't insert on a space
                    mangledPan += '*';
                    i++;
                }
                else {
                    mangledPan += pan.charAt(pos);
                }
                pos++;
            }while (i < 4 && pos < pan.length());
            mangledPan += pan.substring(pos, pan.length());
        }

        return mangledPan;
    }

    /************************ Mask an id *************************************/
    /**
     * Mangle a number by replacing all but the last nn digits with '*'s.
     * Used for APACS70 compliance with merchant id and terminal id.
     */
    public static String applyMask(String strToMask, int mark)
    {
        StringBuffer strMasked = new StringBuffer();

        int pos = strToMask.length();

        // Find position leaving 'mark' number of digits intact, not counting spaces
        for (int i = mark; pos > 0 && i > 0; pos--)
        {
            if (strToMask.charAt(pos - 1) != ' ')
                i--;
        }

        // Copy across first part, maintaining spaces, replacing digits
        for (int i = 0; i < pos; i++)
        {
            if (strToMask.charAt(i) == ' ')
                strMasked.append(' ');
            else
                strMasked.append('*');
        }

        // And complete with the unmasked part
        strMasked.append(strToMask.substring(pos, strToMask.length()));

        return strMasked.toString();
    }
    /********************* Convert to String Array ***********************************/
    /**
     * Converts a vector to an array of strings.
     * @param vector collection of strings
     * @return array of strings
     */
    public static String[] convertToStringArray(Vector vector)
    {
        return (String[])vector.toArray(new String[vector.size()]);
    }


    /********************* Size String Array *********************************/
    /**
    * Changes the size of a string array.
    */
    public static String[] sizeStringArray(String[] array, int newSize){

        String[] newArray = null;
        if (array == null || newSize > 0){
            newArray = new String[newSize];
            if (newSize > array.length) newSize = array.length;
            for (int i=0; i<newSize; i++)
                newArray[i] = array[i];
        }

        return newArray;

    }



    /*********************** Separate Path Name ******************************/
    /*
    * Seperate a pathname from a full path and filename using known file seperator
    * characters.
    */
    public static String separatePathName(String fullName){

        String path = null;

        if (fullName != null){

            try {
                //Standard character first
                path = fullName.substring(0,fullName.lastIndexOf(File.separatorChar));
            }catch(StringIndexOutOfBoundsException e){
                try {
                    //Back slash
                    path = fullName.substring(0,fullName.lastIndexOf('\\'));
                }catch(StringIndexOutOfBoundsException e1){
                    try {
                        //Forward slash
                        path = fullName.substring(0,fullName.lastIndexOf('/'));
                    }catch(StringIndexOutOfBoundsException e2){
                    }
                }
            }
        }

        return path;
    }

    /*********************** Separate File Name ******************************/
    /*
    * Seperate a filename from a full path and filename using known file seperator
    * characters.
    */
    public static String separateFileName(String fullName){

        String file = null;

        if (fullName != null){

            try {
                //Standard character first
                int index = fullName.lastIndexOf(File.separatorChar) + 1;
                if (index > 0) file = fullName.substring(index);
            }catch(StringIndexOutOfBoundsException e){}

            if (file == null){
                try {
                    //Back slash
                    int index = fullName.lastIndexOf('\\') + 1;
                    if (index > 0) file = fullName.substring(index);
                }catch(StringIndexOutOfBoundsException e){}
            }

            if (file == null){
                try {
                    //Forward slash
                    int index = fullName.lastIndexOf('/') + 1;
                    if (index > 0) file = fullName.substring(index);
                }catch(StringIndexOutOfBoundsException e){}
            }


        }

        return file;
    }

    /******************************* Compact Postcode ***********************************
     * This routine simply makes sure the given postcode is uppercase and contains no
     * whitespace anywhere within the string.
     */
    public static String compactPostcode(String postcode)
    {
        String s = postcode.toUpperCase();
        String r = "";

        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(!Character.isWhitespace(c)) r = r + c;
        }

        return r;
    }

    /******************************* Remove Whitespace ***********************************
     * This routine removes whitespace from the given parameter
     * @param in the input string
     * @return in, with the whitespace removed.
     */
    public static String removeWhitespace(String in)
    {
        String retVal = "";

        for(int i = 0; i < in.length(); i++)
        {
            char c = in.charAt(i);
            if(!Character.isWhitespace(c)) retVal += c;
        }

        return retVal;
    }



//    /************************ Load From File *********************************/
//    /**
//    * Load a string from a file.
//    */
//    public static String loadFromFile(InputStream in) throws IOException
//    {
//        StringBuffer result = new StringBuffer();
//        Reader fileReader = null;
//
//        try {
//
//        	fileReader = new InputStreamReader(in);
//
//            //Read in 4k chunks
//            char[] buffer = new char[4096];
//            int len;
//            while ((len=fileReader.read(buffer)) != -1){
//                result.append(new String(buffer, 0, len));
//            }
//
//            return result.toString();
//
//        } catch (FileNotFoundException e) {
//            result = null;
//            Logger.writeLog("StringFunctions",e,Logger.LOG_INFORMATION);
//            throw new IOException("File not found.");
//        } catch (IOException e){
//        	Logger.writeLog("StringFunctions",e,Logger.LOG_INFORMATION);
//            result = null;
//            throw(e);
//        } finally {
//            if (fileReader != null) fileReader.close();
//        }
//
//    }

//    /************************ Load From File *********************************/
//    /**
//    * Load a string from a file.
//    */
//    public static String loadFromFile(String fileName) throws IOException
//    {
//        return loadFromFile(new FileInputStream(fileName));
//    }
//
//    /******************** Separate Into Lines *******************************/
//    /**
//     * Format a long text message into multiple lines such that words are not
//     * split across line ends. 'Words' are considered delimited with space only.
//     * Explicit newlines are respected and attempt is made to ensure that where
//     * a generated line break would coincide with an explicit newline only a
//     * single line break is inserted.
//     * All returned lines are stripped of whitespace at beginning and end so can
//     * be centralised by the caller if desired. Empty lines can be returned when
//     * explicit newlines are used.
//     * A lot of string copying is used - could be more efficient!
//     * @return lines as array of Strings, zero length array if nothing to return
//     */
//
//    public static String[] separateIntoLines(String string, int maxLength, boolean trim)
//    {
//        Vector v = new Vector();
//
//		if (maxLength > 0)
//		{
//			String source = string;
//			while (source != null && source.length() > 0)
//			{
//				int pos;
//				boolean foundnl;
//				String s;
//
//				// First allow explicit newlines to break up message into lines
//				for (pos = 0, foundnl = false; pos < maxLength && pos < source.length() && !foundnl; pos++)
//				{
//					if (source.charAt(pos) == '\n')
//					{
//						// found a newline within line length - use this substring
//						foundnl = true;
//						s = source.substring(0, pos);
//						if (trim) s = s.trim();       // remove all leading and trailing space before
//						v.addElement(s);    // adding to vector - but allow empty lines
//
//						source = source.substring(pos + 1);     // amd remove from source
//					}
//				}
//
//				if (!foundnl)     // Ok, no newline in the first maxlength chars, search backwards for spaces
//				{
//					if (source.length() <= maxLength)
//					{                               			// It's the last lump, just add it whole
//						if (trim) source = source.trim();       // remove leading and trailing space
//						if (source.length() > 0)                // and add to vector if non-empty
//							v.addElement(source);
//						source = null;
//					}
//					else    // string is at least maxLength + 1 long
//					{
//						// handle special case where \n is at maxLength - we don't
//						// want to introduce a break where it's explicit
//						if (source.charAt(maxLength) == '\n')
//						{
//							s = source.substring(0, maxLength);
//							if (trim) s = s.trim();
//							v.addElement(s);      // add line and remove with \n from source
//							source = source.substring(maxLength + 1);
//						}
//						else
//						{
//							// find the last space before or at the end of this line
//							if ((pos = source.lastIndexOf(' ', maxLength)) != -1)
//							{
//								s = source.substring(0, pos);
//								if (trim) s = s.trim();           // remove leading and trailing space
//								if (s.length() > 0)               // and add to vector if not empty
//									v.addElement(s);
//
//								source = source.substring(pos + 1);    // and remove from source
//							}
//							else
//							{       // no spaces in this bit - if its the end, just add it
//								if (source.length() <= maxLength)
//								{
//									v.addElement(source);
//									source = null;
//								}
//								else    // add one line's worth then continue
//								{
//									s = source.substring(0, maxLength);
//									v.addElement(s);
//									source = source.substring(maxLength);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//
//        // Convert to string array - may return an array of zero length
//        return convertToStringArray(v);
//    }
    
    
    
//    public static String[] separateIntoLines(String string, int maxLength)
//    {
//    	return separateIntoLines(string, maxLength, true);
//    }




    /************************ Is Numeric ? ***********************************
    /**
    * Determins if a string contains all numeric characters.
    */
    public static boolean isNumeric(String string)
    {
    	if (string!=null) {
    		int length = string.length();
    		char character;

    		for(int i=0; i<length; i++){
    			character = string.charAt(i);
    			if (character < '0' || character > '9')
    				return false;
    		}

    		return true;
    	}
    	return false;
    }

    /**
     * Determines if the string contains only alpha characters.
     */
    public static boolean isAlpha(String string)
    {
        StringBuffer buffer = new StringBuffer(string);
        for (int i = 0; i < buffer.length(); ++i)
        {
            if (!Character.isLetter(buffer.charAt(i)))
                return false;
        }

        return true;
    }

    /**
     * Determines if the string contains only alpha numeric characters.
     */
    public static boolean isAlphaNumeric(String string)
    {
        StringBuffer buffer = new StringBuffer(string);
        for (int i = 0; i < buffer.length(); ++i)
        {
            if (!Character.isLetterOrDigit(buffer.charAt(i)))
                return false;
        }

        return true;
    }

    /********************************* is Word ******************************/
    /**
     * determine if the string contains only alphabetic characters
     * 
     * @param string
     * @return
     */
    public static boolean isWord(String string) {
        
        StringBuffer buffer = new StringBuffer(string);
        for (int i = 0; i < buffer.length(); ++i) {
            if (!Character.isJavaLetter(buffer.charAt(i)))
                return false;
        }
        return true;
    }
    
    /****************************** Is Ascii *********************************/
    /**
    * Verifies that string contains only ascii characters.
    * @param str string to test
    * @return true if string is all ascii chars
    */
    public static boolean isAscii(String str)
    {
        if (str != null) {
			int length = str.length();
            for (int i = 0; i < length; i++)
                if (str.charAt(i) < 0x0020 || str.charAt(i) > 0x007F)
                    return false;
        }

        return true;
    }


    /****************************** Remove Non Ascii *********************************/
    /**
    * Strips out any non-ascii characters from the passed in string
    * @param str string to test
    * @return String converted string
    */
    public static String removeNonAscii(String string)
    {
		StringBuffer buf = new StringBuffer();
		if (string != null)
		{
			int length = string.length();

			for (int i = 0; i < length; i++) {
				char character = string.charAt(i);
				// if this is an ascii character then add it to the string buffer
                if (character >= 0x0020 && character <= 0x007F)
					buf.append(character);
			}
		}
		return buf.toString();
    }


	/************************** Get Numeric Content **************************/
	/**
	 * Return a string containing only the numeric content from the supplied
	 * string.
	 * @param string string
	 * @return new string containing only numeric data from the supplied string,
	 * returns "" if no numeric content.
	 */
	public static String getNumericContent(String string)
	{
		StringBuffer buf = new StringBuffer();
		if (string != null)
		{
			int length = string.length();

			for (int i = 0; i < length; i++) {
				char character = string.charAt(i);
				if (character >= '0' && character <= '9')
					buf.append(character);
			}
		}
		return buf.toString();
	}



    /*********************** Encode URL Parameter ****************************/
    /**
    * This method should be used to encode URL parameters which may contain
    * characters that have special significance to browsers.  It should not
    * be used to encode the complete URL otherwise the "?", "&", and "="
    * characters will get encoded as well.
    *
    * THIS METHOD IS NOW REDUNDANT
    */
    public static String encodeUrlParameter(String parameter)
    {
/**
        parameter = FindAndReplace(parameter, "%", "%25");      // must do this first !
        parameter = FindAndReplace(parameter, "+", "%2B");
        parameter = FindAndReplace(parameter, ";", "%3B");
        parameter = FindAndReplace(parameter, "/", "%2F");
        parameter = FindAndReplace(parameter, "?", "%3F");
        parameter = FindAndReplace(parameter, ":", "%3A");
        parameter = FindAndReplace(parameter, "@", "%40");
        parameter = FindAndReplace(parameter, "=", "%3D");
        parameter = FindAndReplace(parameter, "&", "%26");
        parameter = FindAndReplace(parameter, "<", "%3C");
        parameter = FindAndReplace(parameter, ">", "%3E");
        parameter = FindAndReplace(parameter, "\"", "%22");
        parameter = FindAndReplace(parameter, "#", "%23");
        return parameter;
 */
        return URLEncoder.encode(parameter);
    }


    /********************* Contains unsafe URL characters ********************/
    /**
     *  Determines if a URL contains "unsafe" characters(see RFC 1738). The %
     *  character is permitted here as it can be used to encode characters.
     */
    public static boolean containsUnsafeURLCharacters(String url)
    {
        String unsafeCharacters = " <>\"#|\\^~[]`";
        int length = unsafeCharacters.length();
        char character;

        for(int i = 0; i < length; i++)
        {
            character = unsafeCharacters.charAt(i);
            if(url.indexOf(character) != -1){
                return true;
            }
        }

        return false;
    }





	/************************* Test Pattern *********************************/
	/**
	* Tests to see if a supplied string matches a pattern. The pattern
	* characters are shown below:
	*   A-Z and a - z, numbers and symbols          Characters are matched in the string
	*   !                                           Mandatory numeric
	*   #                                           Optional numeric
	*   &                                           Mandatory alpha
	*   *                                           Optional alpha
   */
	public static boolean testPattern(String str, String pattern)
	{
		boolean result = true;

		//Start at the right side of the pattern string and work backwards:
		int i = pattern.length()-1;
		int j = str.length()-1;
		char patternChar;
		char strChar;

		while (i >= 0){

			patternChar = pattern.charAt(i);

			if (j >= 0){
				strChar = str.charAt(j);
			}
			else {
				strChar = 0;
			}

			switch (patternChar){

				//Mandatory numeric
				case '!':
					if (j >= 0 && strChar >= '0' && strChar <= '9'){
						i--;
						j--;
					}
					else {
						result = false;
					}
					break;


				//Optional numeric...
				case '#':
					if (j >= 0){
						//Have a character so test it
						if (strChar >= '0' && strChar <= '9'){
							i--;
							j--;
						}
						else {
							//Not a match but is optional so move up the pattern
							i--;
						}
					}
					else {
						//No string characters left so just move up the pattern
						i--;
					}
					break;


				//Mandatory alpha...
				case '&':
					if (j >= 0 && ((strChar >= 'a' && strChar <= 'z') || (strChar >= 'A' && strChar <= 'Z'))){
						i--;
						j--;
					}
					else {
						result = false;
					}
					break;


				//Optional alpha...
				case '*':
					if (j >= 0){
						//Have a character so test it
						if ((strChar >= 'a' && strChar <= 'z') || (strChar >= 'A' && strChar <= 'Z')){
							i--;
							j--;
						}
						else {
							//Not a match but is optional so move up the pattern
							i--;
						}
					}
					else {
						//No string characters left so just move up the pattern
						i--;
					}
					break;


				//Otherwise must match the pattern character extactly...
				default:
					if (strChar == patternChar){
						i--;
						j--;
					}
					else {
						result = false;
					}
					break;
			}

			if (result == false){
				break;
			}

		}//While...

		//Check that the supplied string is not too long
		if (j >= 0){
			result = false;
		}
		return result;
	}






    /*********************** Format SQL **************************************/
    /**
     * Formats SQL to make it easier to read and to enable pasting from console window
     * into Enterprise Manager without manual reformatting.
     * This method could be enhanced to avoid splitting lines in the middle of timestamps,
     * and to split lines at SQL keywords if possible.
     */
    public static String formatSql(String sql)
    {
        final int MAX_LINE_LENGTH = 120;
        String oldSql = sql;
        String newSql = "";
        int space;

        try {
            while (oldSql.length() > 0)
            {
                if (oldSql.length() <= MAX_LINE_LENGTH) {
                    newSql += oldSql;
                    oldSql = "";
                } else {
                    // Last space within range, or first space afterwards
                    if ((space = oldSql.lastIndexOf(" ", MAX_LINE_LENGTH)) <= 0)
                        space = oldSql.indexOf(" ", MAX_LINE_LENGTH);

                    if (space > 0) {
                        // We've found a space - split here
                        newSql += oldSql.substring(0,space) + "\n";
                        oldSql = oldSql.substring(space);
                    } else {
                        // We couldn't find a space - output remaining SQL on one line
                        newSql += oldSql;
                        oldSql = "";
                    }
                }
            }
            return newSql;
        } catch (Exception e) {
            return sql;
        }
    }


    /*********************** Format XML **************************************/
    /**
     * Formats XML to make it easier to read and to enable pasting from console window
     * into a text file for viewing in Internet Explorer without manual reformatting.
     */
    public static String formatXML(String xml)
    {
        final int MAX_LINE_LENGTH = 120;
        String oldXml = xml;
        String newXml = "";
        int boundary;

        try {
            while (oldXml.length() > 0)
            {
                if (oldXml.length() <= MAX_LINE_LENGTH) {
                    newXml += oldXml;
                    oldXml = "";
                } else {
                    // Last tag boundary within range, or first afterwards
                    if ((boundary = oldXml.lastIndexOf("><", MAX_LINE_LENGTH)) <= 0)
                        boundary = oldXml.indexOf("><", MAX_LINE_LENGTH);

                    if (boundary > 0) {
                        // We've found a boundary - split here
                        newXml += oldXml.substring(0,boundary+1) + "\n";
                        oldXml = oldXml.substring(boundary+1);
                    } else {
                        // We couldn't find a boundary - output remaining XML on one line
                        newXml += oldXml;
                        oldXml = "";
                    }
                }
            }
            return newXml;
        } catch (Exception e) {
            return xml;
        }
    }


//    /************************** Encode Redirect URL ***************************/
//    /**
//     * Makes sure that any spaces and special characters are correctly encoded
//     * in the redirect URL.
//     */
//    public static String encodeRedirectURL(String url)
//    {
//        // First search for a '?'. We only encode data past this.
//        int questionMark = url.indexOf('?');
//        if(questionMark != -1)
//        {
//            String prefix = url.substring(0, questionMark + 1);
//            String suffix = url.substring(questionMark + 1);
//            String tail = "";
//
//            /* NAUGTHY URL CODE: This code has not been included so that we are made
//             * aware of all problem URLs. However, it's been left here just in case
//             * we need it. It will not work fully with double encoded URLs!
//             *
//            // Check the suffix to see if another (illegal) question mark exists.
//            questionMark = suffix.indexOf('?');
//            if(questionMark != -1)
//            {
//                // This is probably a old-style naughty URL in the following form:
//                // http://server/page?method=open&page=http://server/page?method=return@blah...
//
//                // If this is the case we need to remove the tail parameter and encode it separately.
//                int lastParam = suffix.substring(0, questionMark).lastIndexOf('=');
//
//                tail = URLEncoder.encode(suffix.substring(lastParam + 1));
//                suffix = suffix.substring(0, lastParam + 1);
//            }
//            */
//
//            // Break the suffix at each '&'.
//            String[] parameters = splitString(suffix, "&");
//            for(int i = 0; i < parameters.length; i++)
//            {
//                // Break each parameter into name/value pairs.
//                if(i != 0) prefix = prefix + "&";
//                String[] nameValue = splitString(parameters[i], "=");
//                if(nameValue.length == 1)
//                {
//                    prefix = prefix + URLEncoder.encode(parameters[i]);
//                }
//                else if(nameValue.length == 2)
//                {
//                    prefix = prefix + URLEncoder.encode(nameValue[0]) + "=" + URLEncoder.encode(nameValue[1]);
//                }
//                else
//                {
//                    // This must be an invalid URL.
//                    Logger.writeLog("StringFunctions.encodeRedirectURL()", new Exception("Invalid URL " + url), Logger.LOG_INFORMATION);
//                    return url;
//                }
//            }
//
//            return prefix + tail;
//        }else{
//            return url;
//        }
//    }


    /**
     *  converts a int array into a single string with separators
     */
    public static String intArrayToDelimitedString(int[] array, String separator)
    {
        String statusesString = "";
        for (int i = 0; i < array.length; i++) {
            if (i > 0) statusesString += separator;
            statusesString += array[i];
        }

        return statusesString;
    }


    /********************* String Array to Delimited String ******************/
    /**
     *  converts a string array into a single string with separators
     */
    public static String stringArrayToDelimitedString(String[] array, String separator)
    {
        String statusesString = "";
        for (int i = 0; i < array.length; i++) {
            if (i > 0) statusesString += separator;
            statusesString += array[i];
        }

        return statusesString;
    }

    /********************* boolean Array to Delimited String ******************/
    /**
     *  converts a boolean array into a single boolean with separators
     */
    public static String booleanArrayToDelimitedString(boolean[] array, String separator)
    {
        String statusesString = "";
        for (int i = 0; i < array.length; i++) {
            if (i > 0) statusesString += separator;
            statusesString += array[i];
        }

        return statusesString;
    }




    /*********************** Get IP Address String ******************************/
    /**
    * Get a string representation of a byte array IP address.
    */
    public static String getIPAddressString(byte[] IPAddress){

        return ((int)IPAddress[0] & 0xFF) + "." + ((int)IPAddress[1] & 0xFF) + "." + ((int)IPAddress[2] & 0xFF) + "." + ((int)IPAddress[3] & 0xFF);

    }


    /**
     * Replaces all illegal filename characters with underscore.
     */
    public static String removeIllegalFilenameChars(String filename)
    {
    	return removeIllegalFilenameChars(filename, "_");
    }
    
    
    /**
     * Replaces all illegal filename characters with the given replacement string.
     */
    public static String removeIllegalFilenameChars(String filename, String replacement)
    {
        String invalidChars = "\\/&?*\":'";
        String replaceWith = replacement;

    	// First check that the replacement character itself is not an illegal character
        // If it is then use an underscore instead
        if (!replacement.equals("") && invalidChars.indexOf(replacement) != -1)
        	 replaceWith = "_";
        	
        if(filename == null) return null;

        String s = "";
        for(int i = 0; i < filename.length(); i++)
        {
            if(invalidChars.indexOf(filename.charAt(i)) != -1)
            {
                s = s + replaceWith;
            }else{
                s = s + filename.substring(i, i + 1);
            }
        }

        return s;
    }

    /**
     * @param str
     * @return
     */
    public static String formatQuoted(String str) {
    	
    	return str!=null ? "\""+str+"\"" : "";
    }
    
    /**
     * Test a <code>String</code> for quotes, returning a new <code>String</code> formatted to display the quotes
     * as is or as <code>"_"</code> if there are invalid displayable characters.
     * @param str a <code>String</code> to test.
     * @return the reformatted <code>String</code>.
     */
    public static String formatStringTestQuotes(String str)
    {
        String invalidChars = "\'";
        String charsToReplace = "<>\"";

        if(str == null) return null;

        String s = "";
        for(int i = 0; i < str.length(); i++)
        {
            if(invalidChars.indexOf(str.charAt(i)) != -1)
            {
                s = s  + "\\" + str.charAt(i);
            }else if(charsToReplace.indexOf(str.charAt(i)) != -1){
                s = s + "_";
            }else{
                s = s + str.substring(i, i + 1);
            }
        }

        return s;
    }


    /**
     * Compares two dot-separated version strings e.g. "a.b.c" or "1.2.3.4" etc
     * Returns:
     *  -1 if string1 is less than string2
     *   0 if both strings are the same
     *   1 if string1 is greater than string 2
     */
    public static int compareVersionStrings(String string1, String string2)
    {
        // If the strings are the same we can return straightaway
        if (string1.equals(string2))
            return 0;

        // Split each string into an array of strings
        String[] s1 = splitString(string1, ".");
        String[] s2 = splitString(string2, ".");
        int intValue1 = 0;
        int intValue2 = 0;

        // Now iterate through each element of the first array and compare each one with the respective element of the second array
        for (int i = 0; i < s1.length; i++)
        {
            // If there are no more elements in the second array then the strings have been equal so far but the first string wins because it is longer
            // e.g. s1 might be "1.2.3.4" and s2 might be "1.2.3"
            if (s2.length <= i)
                return 1;

            // We need to decide by looking at the element whether to do integer or string comparison

            // Try and parse the element of the first array as an integer
            try {
                intValue1 = Integer.parseInt(s1[i]);
                // If got here then parsed as an integer - get the same element of string2 and try and parse that as an integer
                try {
                    intValue2 = Integer.parseInt(s2[i]);
                } catch (NumberFormatException nfe) {
                    // If the same element of string2 is not an integer then we are forced to perform string comparison on the two elements
                    int result = s1[i].compareToIgnoreCase(s2[i]);
                    if (result < 0)
                        return -1;
                    else if (result > 0)
                        return 1;
                    else
                        continue; // both equal so continue evaluating the rest of the string
                }
                // We now have both integers so we can perform simple integer comparison on the two elements
                if (intValue1 > intValue2)
                    return 1;
                else if (intValue1 < intValue2)
                    return -1;
                else
                    continue; // both equal so continue evaluating the rest of the string
            } catch (NumberFormatException nfe) {
                // The element could not be parsed as an integer so use normal string comparison
                int result = s1[i].compareToIgnoreCase(s2[i]);
                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                else
                    continue; // both equal so continue evaluating the rest of the string
            }
        }

        // If we reach here then we have finished evaluating the first string so, presuming the strings are the same length, they must be equal
        // Otherwise string 2 is greater e.g. string1 might be "1.2.3" and string2 might be "1.2.3.4"
        if (s2.length > s1.length)
            return -1;
        else
            return 0;
    }




    /********************* Convert to Byte Array ******************************/
    /**
    * Convert a non encoded byte array.
    */
    public static byte[] toByteArray(String str){

        int offset = 0;
        byte[] array = new byte[str.length()*2];
        for (int i=0; i< str.length(); i++){
            array[offset] = (byte)(0x00FF & str.charAt(i));
            offset++;
            array[offset] = (byte)((0xFF00 & str.charAt(i))>>8);
            offset++;
        }
        return array;

    }


    /********************* Convert from Byte Array ****************************/
    /**
    * Convert a non encoded byte array to a string.
    */
    public static String fromByteArray(byte[] array){

        StringBuffer buffer = new StringBuffer();
        for (int i=0; i< array.length; i+=2){
            buffer.append((char)((0xFF00 & ((int)array[i+1])<<8) + (0x00FF & (int)array[i])));
        }
        return buffer.toString();

    }





    /************************** Get New URL **********************************/
    /**
    * Get a new URL by combining the current one and the new one.
    */
    public static String getNewUrl(String currentUrl, String newUrl){

        String result;

        if (currentUrl == null){
            result = newUrl;
        }

        else if (newUrl == null || newUrl.length() == 0){
            result = currentUrl;
        }

        else if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            result = newUrl;
        }

        else if (currentUrl.startsWith("http://") || currentUrl.startsWith("https://")) {
            if (newUrl.startsWith("/")){
                //Strip of the host and add the new
                int index = currentUrl.startsWith("https://") ? currentUrl.indexOf("/",8) : currentUrl.indexOf("/",7);
                if (index > 0){
                    result = currentUrl.substring(0, index) + newUrl;
                }
                else {
                    result = currentUrl + newUrl;
                }
            }
            else {
                //Strip off last part of the url and add the new
                int index = currentUrl.lastIndexOf("?");

                if (index == -1)
                    index = currentUrl.lastIndexOf("/");
                else
                    index = currentUrl.lastIndexOf("/",index);

                result = currentUrl.substring(0, index+1) + newUrl;
            }
        }
        else {
            result = currentUrl + newUrl;
        }

        //remove any ../ directories
        int dotDotPos = result.indexOf("/../");

        while (dotDotPos!=-1)
        {
            result = result.substring(0,result.lastIndexOf("/",dotDotPos - 1)) + result.substring(dotDotPos + 3,result.length());
            dotDotPos = result.indexOf("/../");
        }

        return result;

    }


//    /*********************** Get HTTP Request Parameter **********************/
//    /**
//    * Get a request parameter from an HTTP request and apply any conversion
//    * necessaary for encoding. This is because some web server (like Tomcat) do
//    * not perform encoding correctly. Also only version 2.3 of the servlet spec
//    * enables the encoding to be specified anyway.
//    */
//    public static String getHttpRequestParameter(Object request, String parameterName){
//
//        //If the encoding in configuration is not the same as the platform
//        //then we need to convert.
//        //String param = request.getParameter(parameterName);
//
//        String param = null;
//        try{
//            java.lang.reflect.Method method = request.getClass().getMethod("getParameter", new Class[] {String.class});
//            method.setAccessible(true);
//            param = (String)method.invoke(request, new Object[] {parameterName});
//        }catch(Throwable t){
//            System.out.println("StringFunctions: Failed to call getParameter(String parameterName) on " + request);
//        }
//
//        if (param!=null)
//        {
//            if (ConfigServer.getReencodeServerRequests()){
//                try {
//                    String tempStr = param;
//                    byte[] temp = param.getBytes(ConfigServer.getServerRequestCharsetName());
//                    //System.out.print("Intermediate bytes:"); for(int y=0;y<temp.length;y++){System.out.print(" 00" + StringFunctions.byteToHex(temp[y]));}System.out.println();
//                    param = new String(temp, "UTF8");
//                    //System.out.println("Converted in hex:" + StringFunctions.stringToHex(param));
//                    if ((param == null || param.length() == 0) && tempStr.length() != 0) {
//                        Logger.writeLog("StringFunctions.getHttpRequestParameter", "Failed to convert " + tempStr, Logger.LOG_WARNING);
//                        param = tempStr;
//                    }
//                }catch(UnsupportedEncodingException uee){
//                    Logger.writeLog("StringFunctions.getHTTPRequestParameter", uee, Logger.LOG_WARNING);
//                }
//            }
//        }
//
//        return param;
//
//    }
//
//
//    /*********************** Get HTTP Request Parameter Values ***************/
//    /**
//     * Get an array of request parameters from an HTTP request and apply any conversion
//     * necessaary for encoding. This is because some web servers (like Tomcat) do
//     * not perform encoding correctly. Also only version 2.3 of the servlet spec
//     * enables the encoding to be specified anyway.
//     */
//    public static String[] getHttpRequestParameterValues(Object request, String parameterName)
//    {
//        // If the encoding in configuration is not the same as the platform then we need to convert.
//        // String[] values = request.getParameterValues(parameterName);
//
//        String[] values = null;
//        try {
//            java.lang.reflect.Method method = request.getClass().getMethod("getParameterValues", new Class[] {String.class});
//            method.setAccessible(true);
//            values = (String[])method.invoke(request, new Object[] {parameterName});
//        } catch (Throwable t) {
//            System.out.println("StringFunctions: Failed to call getParameterValues(String parameterName) on " + request);
//        }
//
//        if (values != null)
//        {
//            if (ConfigServer.getReencodeServerRequests()){
//                try {
//                    String tempStr = null;
//                    for (int i = 0; i < values.length; i++) {
//                        tempStr = values[i];
//                        byte[] temp = values[i].getBytes(ConfigServer.getServerRequestCharsetName());
//                        values[i] = new String(temp, "UTF8");
//                        if ((values[i] == null || values[i].length() == 0) && tempStr != null && tempStr.length() != 0){
//                            Logger.writeLog("StringFunctions.getHTTPRequestParameterValues", "Failed to convert " + tempStr, Logger.LOG_WARNING);
//                            values[i] = tempStr;
//                        }
//                    }
//                } catch (UnsupportedEncodingException uee) {
//                    Logger.writeLog("StringFunctions.getHTTPRequestParameterValues", uee, Logger.LOG_WARNING);
//                }
//            }
//        }
//
//        return values;
//    }
//
//
//    /*********************** Encode HTTP Request Parameter **********************/
//    /**
//    * Encode a request parameter from an HTTP request by encoding each character separately
//    * in such a way that the above method can then be used to decode the parameter
//    */
//    public static String encodeHttpRequestParameter(String parameterValue)
//    {
//        if (parameterValue != null)
//        {
//            if (ConfigServer.getReencodeServerRequests())
//            {
//                try {
//                    byte[] temp = parameterValue.getBytes("UTF8");
//                    parameterValue = new String(temp, ConfigServer.getServerRequestCharsetName());
//                    temp = parameterValue.getBytes(ConfigServer.getServerRequestCharsetName());
//                    StringBuffer encodedParameter = new StringBuffer();
//
//                    for (int y = 0; y < temp.length; y++)
//                    {
//                        // We could encode all characters in hex but this would be unnecessarily verbose and cryptic.
//                        // URLEncoder.encode() only encodes those characters that need encoding, unfortunately there
//                        // are a few characters it encodes incorrectly as 3F i.e. "?"  The workaround is to encode
//                        // each character separately and then check if it might be incorrectly encoded.
//                        String encodedChar = URLEncoder.encode(new String(new byte[]{temp[y]}));
//                        if (encodedChar.equals("%3F"))
//                            encodedChar = "%" + StringFunctions.byteToHex(temp[y]);
//                        encodedParameter.append(encodedChar);
//                    }
//                    return encodedParameter.toString();
//                } catch(UnsupportedEncodingException uee) {
//                    Logger.writeLog("StringFunctions.encodeHTTPRequestParameter", uee, Logger.LOG_WARNING);
//                }
//            }
//        }
//        return parameterValue;
//    }



    /*********************** Build a String from an array of Numbers as String **********************/
    /**
    * This method takes an array of IDs as String such as  ["1", "2", "3", "4"]
    * and returns a String in the following form  (N'1',N'2',N'3',N'4')
    * this is usefull for building arguments to the SQL 'IN' function
    */
    public static String buildInString(String[] ids){
        StringBuffer buffer = new StringBuffer("(");
        int numIds = ids.length;
        for(int i = 0; i < numIds; i++){
        	buffer.append("N'").append(ids[i]).append(i < (numIds - 1)?"',":"'"); //don't put the comma after the last
        }
        buffer.append(")");
        return buffer.toString();
    }

    /*********************** Rotate an array of strings either 90 CW or 90 CCW **********************/
    /**
     * This method will take an array or strings and rotates it either:
     * 90 degrees counterclockwise or
     * 90 degrees clockwise
     * N.B the printer will need to be set correctly to print out the text correctly
     * TODO Might need to do absolute padding on each side for alignment
     *
     * @param int maxWidth, the maxWidth of the input item
     * @param int maxLength, the maxLength of the output item
     * @param String[] itemLines, the original formatted itemLines
     * @param boolean rotateCW, true rotate itemLines Clockwise, else false rotate CounterClockwise
     */
    public static String[] rotateStringArray(int maxWidth, int maxLength, String[] itemLines, boolean rotateCW)
	{

    	int maxLineLength = 0;
    	for (int x=0; x<itemLines.length; x++) {

    		if (itemLines[x]!=null)
    			if (itemLines[x].length()>maxLineLength)
    				maxLineLength = itemLines[x].length();
    	}

		String[] outputLines = new String[maxLineLength];

		for (int line = maxLineLength-1; line >=0 ; line--) { // Must output enough lines to align against the right
			int pos = maxLineLength - line - 1;
			StringBuffer s = null;

			for (int i = 0; i < itemLines.length && itemLines[i] != null; i++) {
				int length = itemLines[i].length();
				char ch;

				if (length > pos) {
					ch = itemLines[i].charAt(pos);

					if (length > pos && ch != ' ') {
						if (s == null)
							s = getBuffer(maxWidth);

						if (rotateCW)// 90 degrees clockwise
							s.setCharAt((maxWidth - i) - 1, ch);
						else
							s.setCharAt(i, ch);
					}
				}
			}

			if (s != null) {
				if (rotateCW) // Do 90 Clockwise Rotation and also pad the lines right
					outputLines[pos] = padLeft(s.toString(), maxLength);
				else // Do 90 CounterClockwise Rotation
					outputLines[line] = s.toString();
			} else {
				if (rotateCW)// Do 90 Clockwise Rotation
					outputLines[pos] = "";
				else// Do 90 CounterClockwise Rotation
					outputLines[line] = "";
			}
		}
		return outputLines;
	}

    /**
     * Gets an StringBuffer filled with spaces " " of length width
     * @param width, the length of the buffer
     * @param str, the string to use, which will be truncated to the int length
     * @return StringBuffer with spaces of length width
     */
	public static StringBuffer getBuffer(int width, String str)
	{
		StringBuffer buf = new StringBuffer(str);
		buf.setLength(width);
		return buf;
	}

	/**
	 * Overloaded method for just specifying the length and returns empty space string
	 * @param width
	 * @return buf
	 */
    public static StringBuffer getBuffer(int width)
	{
    	return getBuffer(width, SPACES);
	}

	/**
	 * Remove spaces from the right of a stringbuffer
	 * @param sb, the StringBuffer to trim
	 * @return sb, the trimmed StringBuffer
	 */
	public static StringBuffer trimRight(StringBuffer sb)
	{
		if (sb == null)
			return sb;

		for (int i = sb.length(); i > 0; i--)
		{
			if (sb.charAt(i - 1) != ' ')
			{
				sb.setLength(i);
				break;
			}
		}
		return sb;
	}

	/**
	 * Concatenate all strings into a StringBuffer, at the end of each String is "\n"
	 * @param lines, the array to join togther.
	 * @return String, Concatenate string of all entries in the string array
	 */
	public static String buildBuffer(String[] lines)
	{
		final StringBuffer sb = new StringBuffer(256);
		for (int j = 0; j < lines.length; j++)
		{
			if (lines[j].length() > 0)
				sb.append(lines[j]);
			sb.append("\n");
		}
		return sb.toString();
	}

    /**
     * If the string is null, return 0. Otherwise return the contents
     * of the first character in the string.
     *
     * @param str
     * @return
     */
    public static char toChar(String str)
    {
        return (str != null && str.length() > 0) ? str.charAt(0) : 0;
    }

    /**
     * If the string is null, return 0. Otherwise return the string
     * converted to an integer
     *
     * @param str
     * @return
     */
    public static int toInt(String str)
    {
        return (str != null) ? Integer.parseInt(str) : 0;
    }
    
    /******************** Replace *********************************************/
    
    /**
     * Replace the first occurrence of <code>oldSubstring</code> in
     * <code>string</code>, if there is one, with <code>newSubstring</code>.
     *
     * @param string - Replace a substring of this String
     * @param oldSubstring - The substring of <code>string</code> to be replaced
     * @param newSubstring - The string to put into <code> string</code>
     * @return A new String which is a copy of <code>string</code> with the first
     *         occurrence of <code>oldSubstring</code> in <code>string</code>, if
     *         there is one, with <code>newSubstring</code>.
     *         Returns null if <code>string</code> is null.
     *         Returns <code>string</code> if either substring is null or
     *         <code>oldSubstring</code> is empty
     */
    public static String replace(String string, String oldSubstring, String newSubstring)
    {
        String result = string;

        if ((string != null) && (string.length() > 0) && (oldSubstring != null) && (oldSubstring.length() > 0) && (newSubstring != null)) {
            int pos = string.indexOf(oldSubstring);
            result = string.substring(0, pos) + newSubstring + string.substring(pos + oldSubstring.length());
        }

        return result;
    }

    /******************** Replace all *****************************************/
    
    /**
     * Replaces all occurrences of <code>oldSubstring</code> in
     * <code>string</code>, if there are any, with <code>newSubstring</code>.
     *
     * @param string - Replace substrings of this String
     * @param oldSubstring - The substring of <code>string</code> to be replaced
     * @param newSubstring - The string to put into <code> string</code>
     * @return A new String which is a copy of <code>string</code> with all
     *         occurrences of <code>oldSubstring</code> in <code>string</code>,
     *         if there are any, with <code>newSubstring</code>.
     *         Returns null if <code>string</code> is null.
     *         Returns <code>string</code> if either substring is null or
     *         <code>oldSubstring</code> is empty
     */
    public static String replaceAll(String string, String oldSubstring, String newSubstring)
    {
        String result = string;

        if ((result != null) && (result.length() > 0) && (result.indexOf(oldSubstring) > -1) && (oldSubstring.length() > 0) && (!oldSubstring.equals(newSubstring))) {

            int start = 0, foundAt = 0;
            while ((foundAt = result.indexOf(oldSubstring, start)) > -1) 
            {
                result = result.substring(0, foundAt) + newSubstring + result.substring(foundAt + oldSubstring.length());
                start = foundAt + newSubstring.length();
            }
        }

        return result;
    }

    /**
     * Converts the input string into a corresponding string containing the ascii
     * codes in two byte hex.
     * 
     * @param string
     * @return
     */
    public static String stringToByteSting(String string)
    {
        if (string == null)
            return null;
        
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < string.length(); ++i)
        {
            String ch = Integer.toString(string.charAt(i), 16);
            if (ch.length() < 2)
                buffer.append("0" + ch);
            else
                buffer.append(ch);
        }             
        
        return buffer.toString();
    }
    
    /**
     * Converts a byte string created using the stringToByteString method back
     * into a normal string
     *  
     * @param byteString
     * @return
     */
    public static String byteStringToString(String byteString)
    {
        if (byteString == null)
            return null;
        
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < byteString.length(); i += 2)
            buffer.append((char)Integer.parseInt(byteString.substring(i,i+2), 16));
        
        return buffer.toString();
    }
    
    /**
     * Test the first string and ensure it contains only the characters supplied
     * by the second string
     * 
     * @param testString
     * @param validChars
     * @return
     */
    public static boolean containsValidChars(String testString, String validChars)
    {
        for (int i = 0; i < testString.length(); ++i)
        {
            if (validChars.indexOf(testString.charAt(i)) == -1)
                return false;
        }
        
        return true;
    }
    
    /**
     * Merge the supplied array onto a single string, seperating them with
     * the supplied delimiter. The delimiter will not be added after the last 
     * string.
     * 
     * @param stringAry
     * @param delimiter
     * @return
     */
    public static String join(String[] stringAry, String delimiter)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < stringAry.length; ++i)
        {
            buffer.append(stringAry[i]);
            if (i + 1 < stringAry.length)
                buffer.append(delimiter);
        }
        
        return buffer.toString();
    }
    
    /**
     * Returns true if the string is null or empty ("").
     * @param string
     * @return boolean
     */
    public static boolean isEmpty(String str)
    {
    	return str == null || str.length() == 0;
    }

    /**
     * Returns true if the string is not null and not empty ("").
     * @param string
     * @return boolean
     */
    public static boolean isNotEmpty(String str)
    {
    	return !isEmpty(str);
    }
    
    /**
     * Return true if the string array is null or empty.
     * @param strArray
     * @return boolean
     */
    public static boolean isEmpty(String[] strArray)
    {
    	return strArray == null || strArray.length == 0;
    }
    
    /**
     * Returns true if the string array is not null and not empty.
     * @param strArray
     * @return boolean
     */
    public static boolean isNotEmpty(String[] strArray)
    {
    	return !isEmpty(strArray);
    }
    
    /**
     * Returns true if the string is null, empty ("") or whitespace.
     * @param str
     * @return boolean
     */
    public static boolean isBlank(String str)
    {
    	return str == null || str.length() == 0 || str.trim().length() == 0;   
    }
    
    /**
     * Returns true if the string is not null and not empty ("") and not whitespace.
     * @param str
     * @return boolean
     */
    public static boolean isNotBlank(String str)
    {
    	return !isBlank(str);
    }
    
    /**
     * Mask a PAN to leave the first 6 and last 4 digits unmasked
     * @param unmasked
     * @return
     */
    public static String manglePAN64(String pan) {
    	String masked = "";
    	
    	if (pan.length() > 6)
    	for (int i = 0; i < pan.length(); ++i) {
    		if (i <= 5 || i >= pan.length() - 4)
    			masked += pan.charAt(i);
    		else
    			masked += "*";
    	}
    	
    	return masked;
    }

    /************************** Get Word *************************************/
    /**
    * Converts the given string array to a Vector.
    */
    public static Vector convertStringArrayToVector(String[] array)
    {
    	Vector v = new Vector();
    	for (int i=0; i < array.length; i++)
    		v.add(array[i]);
    	return v;
    }

    /**
     * Strips out the given character from the passed in string
     * @param str string to test
     * @param char char to remove
     * @return String converted string
     */
    public static String stripCharacter(String string, char c)
    {
    	// copy the string to a new string buffer one character at a time but don't add the character we're stripping
 		StringBuffer buf = new StringBuffer();
 		if (string != null)
 		{
 			int length = string.length();
 			for (int i = 0; i < length; i++) {
 				char character = string.charAt(i);
 				// only add the character if it's not the one we're stripping
                if (character != c)
 					buf.append(character);
 			}
 		}
 		return buf.toString();
     }
    
    /**
     * This method masks the track data
     * 
     * @param trackData
     * @return
     */
    public static String maskTrackData(String trackData)
    {
    	String maskedData = null;
    	if (trackData != null && trackData.length() > 1 && trackData.trim().charAt(0) == ';')
    	{
    		StringBuffer trackDataBuf = new StringBuffer(trackData);
    		String panStr = getPAN(trackData); 
    		if(!isEmpty(panStr))
    		{
    			int startIndex = trackData.indexOf(panStr);
    			int panLen = panStr.length();
    			trackDataBuf.replace(startIndex, panLen+1, fill('*', panLen));
    			maskedData = trackDataBuf.toString();
    		}
    	}
    	return isEmpty(maskedData) ? fill('*', trackData.length()) : maskedData ;
    }
    
    /**
     * Given a list of tags mask the value.
     * Primarily this is used for late removal of track 2 data
     * 
     * @param buffer
     * @param tags
     * @return
     */
    public static String maskTag(String xml, String[] tags) {

     	StringBuffer maskedXML = null;
     	
     	if (xml != null && tags != null) 
     	{
     		maskedXML = new StringBuffer(xml);
     		xml=xml.toUpperCase();
     		for (int i = 0; i < tags.length; i++) 
     		{
     			String xmlElementStartTag = "<" + tags[i];
     			int startOfElement = xml.indexOf(xmlElementStartTag.toUpperCase()) ;
     			String xmlElementEndTag = "</" + tags[i] + ">" ;
     			int endOfElement = 0;
				while(startOfElement!=-1)
				{
					int startOfElementContent = xml.indexOf(">", startOfElement)+1;
					endOfElement = xml.indexOf(xmlElementEndTag.toUpperCase(), startOfElement);
					if ( endOfElement > startOfElement ) 
					{
						int charactersToMask = endOfElement - startOfElementContent;
						maskedXML.replace(startOfElementContent, endOfElement, fill('*', charactersToMask));
					}
					startOfElement = xml.indexOf(xmlElementStartTag,endOfElement+xmlElementEndTag.length());
				}
     		}
     	}
     	return maskedXML != null ? maskedXML.toString() : xml ;
    }
    
    /************************** maskPANinSQL *************************************/
    /**
    * looks for PAN in the SQL and masks the PAN
    */
    public static String maskPANinSQL(String words) {

    	String sql = words.toUpperCase();
		String patternStr = "PAN[\\s]*=[\\s]*[N]*'";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(sql);

		while (matcher.find()) {

			int panStart = matcher.end();
			int panEnd = sql.indexOf("'", panStart);
			String panValue = words.substring(panStart, panEnd);

    		// replace the PAN value with ******
    		words = words.replaceAll(panValue, StringFunctions.fill('*', panValue.length()));
		}
    	return words;
    }

    /******************************** Protocol ********************************/
    /**
     * Returns the protocol for the given url.
     * @param url
     * @return protocol
     */
    public static String getProtocol(String url)
    {
    	String protocol = "";
    	
    	if (url != null)
    		url = url.trim();
    	
    	if (isNotEmpty(url)) {
    		if (url.regionMatches(true, 0, "url:", 0, 4))
    			url = url.substring(4);
    		char c;
    		for (int i = 0; i < url.length() && (c = url.charAt(i)) != '/'; i++) {
    			if (c == ':') {
    				protocol = url.substring(0, i).toLowerCase();
    				break;
    			}
    		}
    	}
    	
    	return protocol;
    }

    /****************************** Replace Left ******************************/
    /**
     * Replaces characters in the string buffer up to a maximum length starting
     * from the right.
     * @param buf
     * @param offset
     * @param length
     * @param str
     */
    public static void replaceLeft(StringBuffer buf, int offset, int length, String str)
    {
    	try {
    		if (str.length() > length)
    			str = str.substring(0, length);
    		buf.replace(offset, offset + str.length(), str);
    	}
    	catch (Exception e) {
    		
    	}
    }

    /***************************** Replace Right ******************************/
    /**
     * Replaces characters in the string buffer up to a maximum length starting
     * from the right.
     * @param buf
     * @param offset
     * @param length
     * @param str
     */
    public static void replaceRight(StringBuffer buf, int offset, int length, String str)
    {
    	try {
        	if (str.length() > length)
        		str = str.substring(str.length() - length, str.length() - 1);
        	else if (str.length() < length)
        		offset += length - str.length();
        	
        	buf.replace(offset, offset + str.length(), str);
    	}
    	catch (Exception e) {
    		
    	}
    }
    
	/************************* Test Regex *********************************/
	/**
	* Tests to see if a supplied string matches the given regular expression.
	* For a definition of the syntax of regular expressions, see
	* http://en.wikipedia.org/wiki/Regular_expression#POSIX_Basic_Regular_Expressions (or elsewhere online)
    */
    public static boolean testRegex(String value, String regex)
    {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
    }
    
    /*************************** Mask String **********************************/
    /**
     * Returns a string of * characters of the length of the input value
     * @param value
     * @return
     */
    public static String maskString(String value)
    {
    	if (isNotEmpty(value)) {
        	StringBuilder sb = new StringBuilder();
        	for (int i = 0; i < value.length(); i++)
        		sb.append("*");
        	return sb.toString();
    	}
    	return "";
    }
    
    /**
     * find email is present or not
     * @param value
     */
    public static boolean isEmailPresent(String value)
    {
    	boolean result = false;
    	String target = value.toUpperCase();
    	if (StringFunctions.isEmpty(value))
    		return false;
    	else {
    		if (target.contains("MAIL") || target.contains("EMAIL") || target.contains("E MAIL") || target.contains("E-MAIL")
    				|| (target.indexOf('@') >= 0 && (target.contains(".COM") || target.contains(".NET") || target.contains(".IN"))))
    		{
    			return true;
    		}
    		else
    			return false;
    	}
    }
    
    /**
     * convert string image data to blob to insert into blob column.
     * 
     * @param data
     * @param connection
     * @return
     * @throws SQLException
     */
    public static Blob getBlobFromString(String data, Connection connection) throws SQLException {
    	if (StringFunctions.isNotBlank(data)) {
			byte[] byteConent = data.getBytes();
			Blob blob = connection.createBlob();
			blob.setBytes(1, byteConent);
			return blob;
		} else {
			return null;
		}
    }
    
    /**
     * convert blob data to string.
     * 
     * @param blob
     * @return
     * @throws SQLException
     */
    public static String getStringFromBlob(Blob blob) throws SQLException {
    	if (blob != null) {
			int blobLength = (int) blob.length();  
			byte[] blobAsBytes = blob.getBytes(1, blobLength);
			blob.free();
			return new String(blobAsBytes);
		} else {
			return null;
		}
    }
    
}
