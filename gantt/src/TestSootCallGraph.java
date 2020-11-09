


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import soot.*;
import soot.jimple.Stmt;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.Chain;

public class TestSootCallGraph extends SceneTransformer {

static LinkedList<String> excludeList;
	
	/** The name of the MySQL account to use (or empty for anonymous) */
	private final static String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final static String password = "root";

	/** The name of the computer running MySQL */  
	
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String dbName = "databasegantt";
	
	/** The name of the table we are testing with */
	private final String tableName = "classes";
	

	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("root", userName);
		connectionProps.put("123456", password);
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/databasegantt"+"?useLegacyDatetimeCode=false&serverTimezone=UTC","root","123456");

		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	/**
	 * Connect to MySQL and do some stuff.
	 * @throws IOException 
	 */
	public void run() throws IOException {
		ResultSet rs = null; 
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}}



		 
		   
		   
	public static void main(String[] args) throws SQLException	{

	
		 
		 
		

			 
		   Connection conn=getConnection();
			Statement st= conn.createStatement();
			Statement st2= conn.createStatement();
			Statement st3= conn.createStatement();
			Statement st4= conn.createStatement();

		 String mypath="net.sourceforge.ganttproject"; 
		 String directory= "C:\\Users\\mouna\\TraceWorkspace\\gantt\\bin"; 
	    String javapath = System.getProperty("java.class.path");
	    String jredir = System.getProperty("java.home")+"\\lib\\rt.jar";
	    String javapath1 = System.getProperty("java.home");
//	    String javapath2=System.getProperty("C:\\Program Files\\Java\\jre7\\lib\\rt.java"); 
	    
	    String path = javapath+File.pathSeparator+jredir+File.pathSeparator+directory +File.pathSeparator+javapath+File.pathSeparator+javapath1;
	 
	    Scene.v().setSootClassPath(path);
	    System.out.println(path);
	    TestSootCallGraph analysis = new TestSootCallGraph();
	    PackManager.v().getPack("wjtp").add(new Transform("wjtp.TestSootCallGraph", analysis));
		 
		 
			    excludeJDKLibrary();

//		
			 
			 
			 //set classpath
	 
	 
	    
	    
   
	   



	    //whole program analysis
	    Options.v().set_whole_program(true);

            //load and set main class
	    Options.v().set_app(true);

	    Scene.v().loadDynamicClasses();

	   List<String> mylist = Arrays.asList(directory); 
//	   System.out.println(mylist);
	    Options.v().set_process_dir(mylist);
	    Scene.v().loadDynamicClasses();
		 System.out.println("hey");
//
        Scene.v().loadNecessaryClasses();
//        Scene.v().loadBasicClasses();

	    System.out.println("Application classes"+Scene.v().getApplicationClasses());
	    System.out.println("classes "+Scene.v().getEntryPoints());
//	    System.out.println("library classes "+Scene.v().getLibraryClasses());
	    System.out.println("dynamic classes"+Scene.v().dynamicClasses());
	    System.out.println("classes"+Scene.v().getClasses());

	    System.out.println("****************");
//	    SootClass appclass = Scene.v().loadClassAndSupport(mainclass); 
//	    appclass.setApplicationClass();
//	    System.out.println("APPCLASS ==="+appclass.isPhantom()+" level "+appclass.resolvingLevel());
//        Scene.v().addBasicClass("Example", SootClass.BODIES);
//
//
//	   
//	    System.out.println("-------------");
//	    
//	    
//	    Scene.v().setMainClass(appclass);
//	    Scene.v().addBasicClass(mainclass);

	    Scene.v().loadNecessaryClasses();
	    Scene.v().loadDynamicClasses(); 
	    Scene.v().loadBasicClasses(); 
	    Scene.v().getActiveHierarchy(); 

	    System.out.println("**********************************");
	    List<String> localList = new ArrayList<String>(); 
	    try {
	    	for ( SootClass myclass: Scene.v().getClasses()) {
		    	System.out.println(myclass.getPackageName());
		    	if(!myclass.toString().contains("java.") && !myclass.toString().contains("jdk.") && !myclass.toString().contains("javax") && !myclass.toString().contains("sun.") 
//		    			&& myclass.getPackageName().contains(mypath)
		    			&& !myclass.getPackageName().contains("soot")
		    			&& !myclass.getPackageName().contains("java")
		    			&& !myclass.getPackageName().contains("com")
		    			
		    			) {
			    	System.out.println("--------------------------------------");

			    	System.out.println(myclass);
			    	System.out.println("--------------------------------------");
			    	String classname=null; 
			    	String classid=null; 
			    	ResultSet classes = st.executeQuery("SELECT * from classes where classname='"+myclass+"'"); 
					while(classes.next()){
						  classname = classes.getString("classname"); 
						  classid= classes.getString("id"); 
						 System.out.println(classname +" "+classid);
			   		   }		
			    	for(SootMethod mymethod: myclass.getMethods()) {
				    	System.out.println("//////////////////////////////////////////////////");
				    	System.out.println(mymethod.getParameterTypes());
				    	System.out.println(mymethod.getReturnType());
			    		System.out.println("METHOD : "+mymethod);
			    		
			    		
			    		String methodname = null; 
			    		String methodid=null; 
			    		String methodparams= mymethod.getParameterTypes().toString().replaceAll("\\s+",""); 
			    		methodparams= methodparams.replaceAll("\\[", "("); 
			    		methodparams= methodparams.replaceAll("\\]", ")"); 
			    		System.out.println(methodparams);
			    		methodname = mymethod.getName(); 
			    		System.out.println(mymethod.getName());
			    		if(methodname.equals("<init>")) methodname="-init-"; 
			    		ResultSet methods = st2.executeQuery("SELECT * from methods where methodname='"+methodname+methodparams+"'and classname='"+classname+"'"); 
						while(methods.next()){
							  methodname = methods.getString("methodname"); 
							  methodid= methods.getString("id"); 
							  System.out.println(methodname +" "+methodid);

				   		   }	
			    		
			    		Body body = null; 
			    		try {
				    		 body = mymethod.retrieveActiveBody(); 

			    		}catch(Exception e) {
			    			
			    		}
			    		if(body!=null) {
			    			for(Local local: body.getLocals()) {
			    				 String localclassid=null; 

			    				if(!local.getType().toString().contains("java") && !local.getType().toString().contains("int")
			    						&& !local.getType().toString().contains("boolean") && !local.getType().toString().contains("char")
			    						&& !local.getType().toString().contains("com") &&  !local.getType().toString().contains("polyglot") ) {
			    					System.out.println("local "+local.getName()+" type "+local.getType());
			    					ResultSet locals = st3.executeQuery("SELECT * from classes where classname='"+local.getType()+"'"); 
									while(locals.next()){
										  localclassid= locals.getString("id"); 
									}
									System.out.println(classname);
									String insert = localclassid +"','" +local.getType()+"','" +myclass+"','" +classid+"','" +methodname+"','" +methodid; 
									System.out.println(insert);
									String mylocal=localclassid+"-"+classid+"-"+methodid; 
									if(!localList.contains(mylocal)) {
										if(localclassid!=null && classid!=null && methodid!=null) {
											String statement= "INSERT INTO `sootfieldmethods`(`fieldtypeclassid`, `fieldtypeclassname`, `ownerclassname`, `ownerclassid`, "
													+ "`ownermethodname`, `ownermethodid`)"
													+ "VALUES ('"+localclassid +"','" +local.getType().toString()+"','" +myclass.toString()+"','" +classid+"','" +methodname+"','" +methodid+"')"; 
											System.out.println(statement);
											st.executeUpdate(statement);
											localList.add(mylocal); 

										}
									}
									
						    		System.out.println("local "+local.getName()+" type "+local.getType());

			    				}
					    	}
			    		}
			   
			    		
			    	}
			    	
					
			    	System.out.println("hey");
		    	}

		    }
	    }catch (Exception e) {
	    	
	    }
	    
	    System.out.println("over");
	    //enable call graph
	    //enableCHACallGraph();
	    //enableSparkCallGraph();

            //start working
//	    PackManager.v().runPacks();
	    System.out.println("over32");
	}
	private static void excludeJDKLibrary()
	{
		 //exclude jdk classes
	    Options.v().set_exclude(excludeList());
		  //this option must be disabled for a sound call graph
	      Options.v().set_no_bodies_for_excluded(true);
	      Options.v().set_allow_phantom_refs(true);
	}
	private static void enableSparkCallGraph() {

		//Enable Spark
	      HashMap<String,String> opt = new HashMap<String,String>();
	      //opt.put("propagator","worklist");
	      //opt.put("simple-edges-bidirectional","false");
	      opt.put("on-fly-cg","true");
	      //opt.put("set-impl","double");
	      //opt.put("double-set-old","hybrid");
	      //opt.put("double-set-new","hybrid");
	      //opt.put("pre_jimplify", "true");
	      SparkTransformer.v().transform("",opt);
	      PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
	}

	private static void enableCHACallGraph() {
		CHATransformer.v().transform();
	}

	private static LinkedList<String> excludeList()
	{
		if(excludeList==null)
		{
			excludeList = new LinkedList<String> ();

			excludeList.add("java.");
		    excludeList.add("javax.");
		    excludeList.add("sun.");
		    excludeList.add("sunw.");
		    excludeList.add("com.sun.");
		    excludeList.add("com.ibm.");
		    excludeList.add("com.apple.");
		    excludeList.add("apple.awt.");
		}
		return excludeList;
	}
	@Override
	protected void internalTransform(String phaseName,
			Map options) {

//		int numOfEdges =0;
//	    enableSparkCallGraph();
//		CallGraph callGraph = Scene.v().getCallGraph();
//		for(SootClass sc : Scene.v().getApplicationClasses()){
//			for(SootMethod m : sc.getMethods()){
//
//		Iterator<MethodOrMethodContext> targets = new Targets(
//				 callGraph.edgesOutOf(m));
//
//				 while (targets.hasNext()) {
//
//					 numOfEdges++;
//
//				SootMethod tgt = (SootMethod) targets.next();
//				 System.out.println(m + " may call " + tgt);
//				 }
//			}
//		}
//
//		 System.err.println("Total Edges:" + numOfEdges);

		 
		 
		 
	      
	}
}