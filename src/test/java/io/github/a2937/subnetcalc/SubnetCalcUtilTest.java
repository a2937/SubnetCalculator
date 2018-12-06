package io.github.a2937.subnetcalc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 
* SubnetCalcUtil Tester. 
* This class
* @author <Authors name> 
* @since <pre>Dec 5, 2018</pre> 
* @version 1.0 
*/ 
public class SubnetCalcUtilTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: calculateNetMaskLength(String netMask) 
* The method takes a standard Subnet mask like
 * 255.255.255.0 and converts it a length instead.
*/ 
@Test
public void testCalculateNetMaskLength() throws Exception
{

    Assert.assertEquals(24,SubnetCalcUtil.calculateNetMaskLength("255.255.255.0"));
} 

/** 
* 
* Method: calculateBroadCastAddress(String ipAddress, String netMask) 
* The method is supposed to be calculate the address that a computers uses
 * to message all the others with.
 */
@Test
public void testCalculateBroadCastAddress() throws Exception
{
    Assert.assertEquals("192.168.0.255",SubnetCalcUtil.calculateBroadCastAddress("192.168.0.2","255.255.255.0"));
} 

/** 
* 
* Method: getRequiredSubNetMaskLengthForHosts(int hostsCount) 
* It takes the host requirements and turns into a subnet mask length.
 * All math is already performed by the program.
*/ 
@Test
public void testGetRequiredSubNetMaskLengthForHosts() throws Exception
{
    Assert.assertEquals(25,SubnetCalcUtil.getRequiredSubNetMaskLengthForHosts(100));
} 

/** 
 *
 * Method: calculateNetworkAddress(String ipAddress, int netMaskLength)
 * The method is virtually identical to calculate network mask with two strings.
 * It takes the length then gets the subnet mask from it first though.
 */
@Test
public void testCalculateNetworkAddressForIpAddressNetMaskLength() throws Exception
{
    Assert.assertEquals("404.404.404.0",SubnetCalcUtil.calculateNetworkAddress("404.404.404.002",24));
} 

/** 
* 
* Method: calculateNetworkAddress(String ipAddress, String netMask) 
*  The method was used as a blueprint for the other function.
*/ 
@Test
public void testCalculateNetworkAddressForIpAddressNetMask() throws Exception
{
    Assert.assertEquals("404.404.404.0",SubnetCalcUtil.calculateNetworkAddress("404.404.404.002","255.255.255.0"));
} 

/** 
* 
* Method: getSubnetMaskFromPrefix(int prefixLength) 
* This particular provides the padding for each grouping of eight bits.
*/ 
@Test
public void testGetSubnetMaskFromPrefix() throws Exception
{
    Assert.assertEquals("255.255.255.000",SubnetCalcUtil.getSubnetMaskFromPrefix(24));
} 

/** 
* 
* Method: getMaximumHostsInSubnet(int maskLength) 
* The formula is 2 to power of the amount of host bits minus 2.
*/ 
@Test
public void testGetMaximumHostsInSubnet() throws Exception
{
  Assert.assertEquals(2046,SubnetCalcUtil.getMaximumHostsInSubnet(21));
} 

/** 
* 
* Method: getMaximumNetmaskForTwoAddresses(String addressOne, String addressTwo) 
* 
*/ 
@Test
public void testGetMaximumNetmaskForTwoAddresses() throws Exception
{
    Assert.assertEquals(25,SubnetCalcUtil.getMaximumNetmaskForTwoAddresses("128.42.5.17","128.42.5.67"));
} 

/** 
 *
 * Method: convertDecimalToBinary(int decimal)
 * In order to test the method, reflection was used.
 * If the method name was to change, this test would
 * no longer unless the name change was updated here too.
*/ 
@Test
public void testConvertDecimalToBinary() throws Exception { 

try { 
   Method method = SubnetCalcUtil.class.getMethod("convertDecimalToBinary", int.class);
   method.setAccessible(true); 
   String binaryReturnValue = (String)method.invoke(null, 69);
   Assert.assertEquals("01000101",binaryReturnValue);
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 

} 

/** 
 *
 * Method: convertBinaryToDecimal(String binary)
 * In order to test the method, reflection was used.
 * If the method name was to change, this test would
 * no longer unless the name change was updated here too.
*/ 
@Test
public void testConvertBinaryToDecimal() throws Exception
{
try
{
   Method method = SubnetCalcUtil.class.getMethod("convertBinaryToDecimal", String.class);
   method.setAccessible(true); 
   int returnValue = (int)method.invoke(null,"1000101");
   Assert.assertEquals(69,returnValue);
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) {
} 

} 

/** 
 *
 * Method: reverse(String input)
 * In order to test the method, reflection was used.
 * If the method name was to change, this test would
 * no longer unless the name change was updated here too.
 */
@Test
public void testReverse() throws Exception
{

try { 
   Method method = SubnetCalcUtil.class.getMethod("reverse", String.class);
   method.setAccessible(true); 
   String returnValue = (String)method.invoke(null, "1000101");
   Assert.assertEquals("1010001",returnValue);
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 

} 

} 
