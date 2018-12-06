package io.github.a2937.subnetcalc;


import java.util.ArrayList;
import java.util.List;

/**
 * A utility designed to help calculate
 * ip addresses and subnet masks for IPv4.
 * IPv6 support will be added as soon as possible.
 */
public class SubnetCalcUtil
{

    /**
     * Calculates the length of the subnet
     * mask if it is in the form ###.###.###.###
     * and there are no padding zeros.
     * Returns -1 if a value cannot be parsed.
     *
     * @param netMask the subnet mask
     * @return the length of the subnet mask.
     * @throws IPException the ip exception thrown if it cannot be separated properly
     */
    public static int calculateNetMaskLength(String netMask) throws IPException
    {
        if(!netMask.contains("."))
        {
            throw new IPException("IPException on SubnetCalcUtil.calculateNetMaskLength " + netMask + " cannot be separated. Use periods to split it into 4 sections.");
        }
        int subnetMaskId = 0;
        String[] octets = netMask.split("\\.");
        for (String octet: octets)
        {
            try
            {
                String binaryOctet = convertDecimalToBinary(Integer.parseInt(octet.replace(" ","")));
                for(char digit: binaryOctet.toCharArray())
                {
                    if(digit == '1')
                    {
                        subnetMaskId++;
                    }
                }
            }
            catch(NumberFormatException nfse)
            {
                return -1;
            }


        }
        return subnetMaskId;
    }

    /**
     * Calculates the broadcast address for a
     * computer on the network given an ip address and a
     * subnet mask. Both must be in the form ###.###.###.###
     * and have no padding digits for zeros.
     * If a string section could not be parsed it returns what has been parsed.
     *
     * @param ipAddress the ip address
     * @param netMask   the net mask
     * @return the broadcast address.
     * @throws IPException the ip exception thrown if it cannot be separated properly
     */
    public static String calculateBroadCastAddress(String ipAddress, String netMask) throws IPException
    {
        if(!netMask.contains(".") || !ipAddress.contains("."))
        {
            throw new IPException("IPException on SubnetCalcUtil.calculateBroadCastAddress " + netMask + " cannot be separated. Use periods to split it into 4 sections.");
        }

        StringBuilder hostBits = new StringBuilder();
        StringBuilder broadCastAddress = new StringBuilder();
        String[] addressOctets = ipAddress.split("\\.");
        String[] octets = netMask.split("\\.");
        for (int i = 0; i < octets.length; i++)
        {
            String str = octets[i];
            String mainStr = addressOctets[i];
            try
            {
                String binaryString = convertDecimalToBinary(Integer.parseInt(str.replace(" ","")));
                for (char bit : binaryString.toCharArray())
                {
                    if (bit == '1')
                    {
                        hostBits.append('0');
                    }
                    else if (bit == '0')
                    {
                        hostBits.append('1');
                    }
                }

                if(!hostBits.toString().contains(Character.toString('1')))
                {
                    broadCastAddress.append(mainStr);
                }
                else
                {

                    broadCastAddress.append(convertBinaryToDecimal(reverse(hostBits.toString())));
                }
                if(i != octets.length - 1)
                {
                    broadCastAddress.append(".");
                }
            }
            catch(NumberFormatException nfse)
            {
                return broadCastAddress.toString();
            }

            hostBits = new StringBuilder();
        }

       return broadCastAddress.toString();
    }

    /**
     * Gets required sub net mask length for an amount of hosts.
     *
     * @param hostsCount the hosts count
     * @return the required sub net mask length for hosts
     */
    public static int getRequiredSubNetMaskLengthForHosts(int hostsCount)
    {

        return 32 - (int)(Math.log((hostsCount))/Math.log(2)+ ((Math.log((hostsCount))%Math.log(2) == 0) ? 0 : 1));
    }


    /**
     * Calculates the address for a
     * network given the IP Address
     * and the subnet mask. The IP address needs to
     * be in the form ###.###.###.###
     * without padding for zeros.
     * <p>
     * If a string section could not be parsed it returns what has been parsed.
     *
     * @param ipAddress     the ip address
     * @param netMaskLength the length of the subnet mask.
     * @return the network address.
     * @throws IPException if the ip address count not be split
     */
    public static String calculateNetworkAddress(String ipAddress, int netMaskLength) throws IPException
    {
        StringBuilder netAddress = new StringBuilder();
        String netMask = getSubnetMaskFromPrefix(netMaskLength);
        if(!ipAddress.contains("."))
        {
            throw new IPException("IPException on SubnetCalcUtil.calculateNetworkAddress " + ipAddress + " cannot be separated. Use periods to split it into 4 sections.");
        }
        String[] maskOctets = netMask.split("\\.");
        String[] addressOctets = ipAddress.split("\\.");
        for (int i = 0; i < addressOctets.length; i++)
        {

            try
            {
                int maskDecimal = Integer.parseInt(maskOctets[i].replace(" ",""));
                String maskBinary = reverse(convertDecimalToBinary(maskDecimal));

                int addressDecimal = Integer.parseInt(addressOctets[i].replace(" ",""));
                String addressBinary = reverse(convertDecimalToBinary(addressDecimal));

                StringBuilder binaryAnded = new StringBuilder();
                for(int w = 0; w < addressBinary.length(); w++)
                {
                    if(addressBinary.charAt(w) == '1' && maskBinary.charAt(w) == '1')
                    {
                        binaryAnded.append("1");
                    }
                    else if(addressBinary.charAt(w) != '1' || maskBinary.charAt(w) != '1')
                    {
                        binaryAnded.append("0");
                    }
                }
                int  combo = convertBinaryToDecimal(binaryAnded.toString());
                netAddress.append(Integer.toString(combo));

                if(i != addressOctets.length - 1)
                {
                    netAddress.append(".");
                }
            }
            catch (NumberFormatException nsfe)
            {
                return netAddress.toString();
            }

        }

        return netAddress.toString();
    }

    /**
     * Calculates the address for a
     * network given the IP Address
     * and the subnet mask. Both need to
     * be in the form ###.###.###.###
     * without padding for zeros.
     *
     * @param ipAddress the ip address
     * @param netMask   the net mask
     * @return the network address.
     * @throws IPException the ip exception
     */
    public static String calculateNetworkAddress(String ipAddress, String netMask) throws IPException
    {
        StringBuilder netAddress = new StringBuilder();
        if(!ipAddress.contains(".") || !netMask.contains("."))
        {
            throw new IPException("IPException on SubnetCalcUtil.calculateNetworkAddress " + ipAddress + " or " + netMask + " "+ " cannot be separated. Use periods to split the address into 4 sections.");
        }
        String[] maskOctets = netMask.split("\\.");
        String[] addressOctets = ipAddress.split("\\.");
       for (int i = 0; i < addressOctets.length; i++)
       {

           try
           {
               String maskBinary = reverse(convertDecimalToBinary(Integer.parseInt(maskOctets[i])));

               String addressBinary = reverse(convertDecimalToBinary(Integer.parseInt(addressOctets[i])));

               StringBuilder binaryAnded = new StringBuilder();
               for(int w = 0; w < addressBinary.length(); w++)
               {
                   if(addressBinary.charAt(w) == '1' && maskBinary.charAt(w) == '1')
                   {
                       binaryAnded.append("1");
                   }
                   else if(addressBinary.charAt(w) != '1' || maskBinary.charAt(w) != '1')
                   {
                       binaryAnded.append("0");
                   }
               }
               int  combo = convertBinaryToDecimal(binaryAnded.toString());
               netAddress.append(Integer.toString(combo));

               if(i != addressOctets.length - 1)
               {
                   netAddress.append(".");
               }
           }
           catch (NumberFormatException nsfe)
           {
               return netAddress.toString();
           }

       }
       return netAddress.toString();
    }


    /**
     * Gets the subnet mask from prefix.
     * An example would be 255.255.255.000
     * if the input was 24.
     *
     * @param prefixLength the prefix length
     * @return the subnet mask from prefix
     */
    public static String getSubnetMaskFromPrefix(int prefixLength)
    {
        StringBuilder dataHolder = new StringBuilder();
        StringBuilder ipAddressBuilder = new StringBuilder();
        int octetsFormed = 0;
        for(int  i = 0 ; i < prefixLength; i++)
        {
            if(i % 8 == 0 && i != 0)
            {
                ipAddressBuilder.append(convertBinaryToDecimal(reverse(dataHolder.toString())));
                dataHolder = new StringBuilder();
                ipAddressBuilder.append('.');
                octetsFormed++;
            }
            dataHolder.append('1');
        }
        if(!dataHolder.toString().isEmpty())
        {
            while(dataHolder.toString().length() != 8)
            {
                dataHolder.append('0');
            }
            if(!ipAddressBuilder.toString().isEmpty() && ipAddressBuilder.toString().charAt(ipAddressBuilder.toString().length() - 1) != '.')
            {
                ipAddressBuilder.append(".");
            }
            ipAddressBuilder.append(convertBinaryToDecimal(dataHolder.toString()));
            octetsFormed++;
            while(octetsFormed != 4)
            {
                ipAddressBuilder.append(".");
                ipAddressBuilder.append("000");
                octetsFormed++;
            }
        }
         return ipAddressBuilder.toString();
    }


    /**
     * Gets the maximum amount of hosts in a subnet.
     * @param maskLength the length of the subnet mask aka number of network bits
     * @return the maximum hosts in subnet
     */
    public static int getMaximumHostsInSubnet(int maskLength)
    {
        int hostBits = 32 - maskLength;
        return  (int)(Math.pow(2,(hostBits)) - 2);
    }

    /**
     * Gets maximum netmask for two addresses.
     * Both addresses must be in the form ###.###.###.###
     * without padded zeros in front of numbers.
     *
     * @param addressOne the address one
     * @param addressTwo the address two
     * @return the maximum netmask for two addresses
     */
    public static  int getMaximumNetmaskForTwoAddresses(String addressOne, String addressTwo) throws IPException
    {
        int maxLength =0;
        if(!addressOne.contains(".") || !addressTwo.contains("."))
        {
            throw new IPException("IPException on SubnetCalcUtil.calculateNetworkAddress " + addressOne + " or " + addressTwo + " "+ " cannot be separated. Use periods to split the address into 4 sections.");
        }
        String[] address1Octets = addressOne.split("\\.");
        String[] address2Octets = addressTwo.split("\\.");
       for (int i = 0; i < address1Octets.length; i++)
       {
           String firstOctet = address1Octets[i].replace(" ","");
           String otherOctet = address2Octets[i].replace(" ","");
           String binaryOneForm = convertDecimalToBinary(Integer.parseInt(firstOctet));
           String binaryTwoForm = convertDecimalToBinary(Integer.parseInt(otherOctet));
           for (int w = 0; w < binaryTwoForm.length(); w++)
           {
                if(binaryOneForm.charAt(w) == binaryTwoForm.charAt(w))
                {
                    maxLength++;
                }
                else
                {
                    return maxLength;
                }
           }

       }
        return maxLength;
    }

    /*
     * It takes the number and if it is
     * divisible by 2, a zero gets added.
     * If there is a remainder of one, meaning it's
     * odd then a one gets added. The String gets padded
     * with zeros if it is not eight characters long.
     * It is designed to convert a single section of an octet.
     */
    private static String convertDecimalToBinary(int decimal)
    {
        StringBuilder binaryString = new StringBuilder();
        int binaryNumber = 0;
        while(decimal > 0)
        {
            binaryNumber = decimal % 2;
            binaryString.append(binaryNumber);
            decimal = decimal / 2;
        }
        while(binaryString.length() < 8)
        {
            binaryString.append('0');
        }

        return binaryString.toString();
    }

    /*
     * It takes a binary string and adds two to the return value
     * if it spots a one in the String. It is designed to do one
     * octet at a time.
     */
    private static int convertBinaryToDecimal(String binary)
    {
        int decimal = 0;
        String reversedBinary = reverse(binary);
        for(int i = reversedBinary.length() - 1; i >= 0 ; i--)
        {
            if(reversedBinary.charAt(i) == '1')
            {
                decimal += Math.pow(2,i);
            }
        }
        return decimal;
    }


    /*
     * This method requires a 16 bit binary string to work.
     * No separators. Just a raw set of 0s and 1s.
     */
    private static String convertBinaryToHex(String binary)
    {
        String reversedBinary = reverse(binary);
        StringBuilder builder = new StringBuilder();
        List<String> sections = new ArrayList<>();
        String firstSection = reversedBinary.substring(0,3);
        sections.add(firstSection);

        String secondSection = reversedBinary.substring(4,7);
        sections.add(secondSection);
        String thirdSection = reversedBinary.substring(8,11);
        sections.add(thirdSection);
        String fourthSection = reversedBinary.substring(12,15);
        sections.add(fourthSection);
        for(int i = sections.size() - 1; i >= 0; i--)
        {
            String currentSection = sections.get(i);

            switch (currentSection)
            {
                case "0000":
                    builder.append("0");
                    break;
                case "0001":
                    builder.append("1");
                    break;
                case "0010":
                    builder.append("2");
                    break;
                case "0011":
                    builder.append("3");
                    break;
                case "0100":
                    builder.append("4");
                    break;
                case "0101":
                    builder.append("5");
                    break;
                case "0110":
                    builder.append("6");
                    break;
                case "0111":
                    builder.append("7");
                    break;
                case "1000":
                    builder.append("8");
                    break;
                case "1001":
                    builder.append("9");
                    break;
                case "1010":
                    builder.append("A");
                    break;
                case "1011":
                    builder.append("B");
                    break;
                case "1100":
                    builder.append("C");
                    break;
                case "1101":
                    builder.append("D");
                    break;
                case "1110":
                    builder.append("E");
                    break;
                case "1111":
                    builder.append("F");
                    break;
                default:
                    break;
            }
        }

        return builder.toString();
    }


    private static String convertHexToBinary(String hex)
    {
        StringBuilder binaryString = new StringBuilder();
        for (char ch: hex.toCharArray())
        {
            switch (ch)
            {
                case '0':
                    binaryString.append('0');
                    break;
                case '1':
                    binaryString.append('1');
                    break;
                case '2':
                    binaryString.append("10");
                    break;
                case '3':
                    binaryString.append("11");
                    break;
                case '4':
                    binaryString.append("100");
                    break;
                case '5':
                    binaryString.append("101");
                    break;
                case '6':
                    binaryString.append("110");
                    break;
                case '7':
                    binaryString.append("111");
                    break;
                case '8':
                    binaryString.append("1000");
                    break;
                case '9':
                    binaryString.append("1001");
                    break;
                case 'A':
                    binaryString.append("1010");
                    break;
                case 'B':
                    binaryString.append("1011");
                    break;
                case 'C':
                    binaryString.append("1100");
                    break;
                case 'D':
                    binaryString.append("1101");
                    break;
                case 'E':
                    binaryString.append("1110");
                    break;
                case 'F':
                    binaryString.append("1111");
                    break;
                default:
                    break;
            }
        }
        return binaryString.toString();
    }


    private static String reverse(String input)
    {
        char[] in = input.toCharArray();
        int begin=0;
        int end=in.length-1;
        char temp;
        while(end>begin)
        {
            temp = in[begin];
            in[begin]=in[end];
            in[end] = temp;
            end--;
            begin++;
        }
        return new String(in);
    }




}
