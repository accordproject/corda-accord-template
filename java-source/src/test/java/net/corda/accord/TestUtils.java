package net.corda.accord;

import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;

public class TestUtils {

	public static TestIdentity ALICE = new TestIdentity(new CordaX500Name("Alice", "TestLand", "US"));
	public static TestIdentity BOB = new TestIdentity(new CordaX500Name("Bob", "TestCity", "US"));
	public static TestIdentity CHARLIE = new TestIdentity(new CordaX500Name("Charlie", "TestVillage", "US"));
	public static TestIdentity MINICORP = new TestIdentity(new CordaX500Name("MiniCorp", "MiniLand", "US"));
	public static TestIdentity MEGACORP = new TestIdentity(new CordaX500Name("MegaCorp", "MiniLand", "US"));
	public static TestIdentity DUMMY = new TestIdentity(new CordaX500Name("Dummy", "FakeLand", "US"));

	// Accord Project Test Utils
	public static TestIdentity DANIEL = new TestIdentity(new CordaX500Name("Daniel", "NY", "US"));
	public static TestIdentity CLAUSE = new TestIdentity(new CordaX500Name("Clause Inc.", "NY", "US"));
	public static TestIdentity JASON = new TestIdentity(new CordaX500Name("Jason", "NY", "US"));
	public static TestIdentity R3 = new TestIdentity(new CordaX500Name("R3 LLC", "NY", "US"));

}