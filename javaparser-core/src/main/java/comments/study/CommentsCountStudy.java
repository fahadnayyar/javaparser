package comments.study;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Iterate over all the Javadoc comments and print them together with a description of the commented element.
 */
public class CommentsCountStudy {

    Map<String, Integer> blockCommentsMap;
    Map<String, Integer> lineCommentsMap;
    Map<String, Integer> javaDocCommentsMap;
    
    int numberOfJavaDocComments;
	int numberOfLineComments;
	int numberofBlockCommments;
	int numberOfErrorFiles;
	int totalNumberOfComments;
	Map<String, Integer> parsedFilesMap;
	 
	
    public CommentsCountStudy() {
    	numberOfJavaDocComments = 0 ;
    	numberOfLineComments = 0;
    	numberofBlockCommments = 0;
    	numberOfErrorFiles = 0;
    	totalNumberOfComments = 0;
    	
    	blockCommentsMap = new HashMap<>();
    	lineCommentsMap = new HashMap<>();
    	javaDocCommentsMap = new HashMap<>();
    	parsedFilesMap = new HashMap<>();
    }
    
	public static void main(String[] args) throws FileNotFoundException{
        
	
		
		//Instantiating the File class
        File file = new File("/Users/fahadnayyar/Desktop/comments_project/rough/comment_parent_types.txt");
        //Instantiating the PrintStream class
        PrintStream stream = new PrintStream(file);
        System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
//        System.setOut(stream);
    	
    	
    	String base_directory = "/Users/fahadnayyar/Desktop/comments_project/"; 
    
    	
    	
    	CommentsCountStudy commentsCountStudy1 = new CommentsCountStudy();
    	File projectDir1 = new File(base_directory + "ExoPlayer");
    	String projectLink1 = "https://github.com/google/ExoPlayer";
    	commentsCountStudy1.studyForComments(projectDir1, projectLink1);
    	commentsCountStudy1.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy2 = new CommentsCountStudy();
    	File projectDir2 = new File(base_directory + "dagger");
    	String projectLink2 = "https://github.com/google/dagger";
    	commentsCountStudy2.studyForComments(projectDir2, projectLink2);
    	commentsCountStudy2.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy3 = new CommentsCountStudy();
    	File projectDir3 = new File(base_directory + "guava");
    	String projectLink3 = "https://github.com/google/guava";
    	commentsCountStudy3.studyForComments(projectDir3, projectLink3);
    	commentsCountStudy3.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy4 = new CommentsCountStudy();
    	File projectDir4 = new File(base_directory + "auto");
    	String projectLink4 = "https://github.com/google/auto";
    	commentsCountStudy4.studyForComments(projectDir4, projectLink4);
    	commentsCountStudy4.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy5 = new CommentsCountStudy();
    	File projectDir5 = new File(base_directory + "error-prone");
    	String projectLink5 = "https://github.com/google/error-prone";
    	commentsCountStudy5.studyForComments(projectDir5, projectLink5);
    	commentsCountStudy5.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy6 = new CommentsCountStudy();
    	File projectDir6 = new File("/Users/fahadnayyar/Desktop/eclipse-workspace/javaparser");
    	String projectLink6 = "https://github.com/javaparser/javaparser";
    	commentsCountStudy6.studyForComments(projectDir6, projectLink6);
    	commentsCountStudy6.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy7 = new CommentsCountStudy();
    	File projectDir7 = new File(base_directory + "closure-compiler");
    	String projectLink7 = "https://github.com/google/closure-compiler";
    	commentsCountStudy7.studyForComments(projectDir7, projectLink7);
    	commentsCountStudy7.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy8 = new CommentsCountStudy();
    	File projectDir8 = new File(base_directory + "gson");
    	String projectLink8 = "https://github.com/google/gson";
    	commentsCountStudy8.studyForComments(projectDir8, projectLink8);
    	commentsCountStudy8.printCommentMap();
    	
    	CommentsCountStudy commentsCountStudy9 = new CommentsCountStudy();
    	File projectDir9 = new File(base_directory + "tink");
    	String projectLink9 = "https://github.com/google/tink";
    	commentsCountStudy9.studyForComments(projectDir9, projectLink9);
    	commentsCountStudy9.printCommentMap();
    
    	
	}
    
    public void studyForComments(File projectDir, String projectLink) {
    	   System.out.println("comments study for github repo: " + projectLink);
    	   studyForCommentsOfType("JavadocComment", projectDir);
           studyForCommentsOfType("LineComment", projectDir);
           studyForCommentsOfType("BlockComment", projectDir);
    }
    
    public void studyForCommentsOfType(String commentType,  File projectDir) {
    	
    	if (commentType.equals("JavadocComment")) {
//        	System.out.println("JavadocComment: ");
    		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(JavadocComment comment, Object arg) {
                            super.visit(comment, arg);
                            String title = null;                            
                            if (parsedFilesMap.get(path) == null) {
                            	parsedFilesMap.put(path, 1);
                            }                           
                            if (comment.getCommentedNode().isPresent()) {
                                title = String.format("%s (%s)", describe(comment.getCommentedNode().get()), path);
                                Optional<Node> parentNode = comment.getParentNode();
                                Optional<Node> commentedNode = comment.getCommentedNode();
                                String commentedNodeClass = commentedNode.get().getClass().toString();
                                //* print the name commented node, it can throw noSuchElementException                        
//                                System.out.println("comment's parent node class: " + commentedNode.get().getClass());                               
                                if (javaDocCommentsMap.get(commentedNodeClass) == null) {
                                	javaDocCommentsMap.put(commentedNodeClass, 0);
                                }
                                javaDocCommentsMap.put(commentedNodeClass, javaDocCommentsMap.get(commentedNodeClass) + 1);                            
                            } else {
                                title = String.format("No element associated (%s)", path);
                                if (javaDocCommentsMap.get("orphanComments") == null) {
                                	javaDocCommentsMap.put("orphanComments", 0);
                                }
                                javaDocCommentsMap.put("orphanComments", javaDocCommentsMap.get("orphanComments") + 1);  
                            }                              
                              //* printing the comment.
//                            System.out.println(title);
//                            System.out.println(Strings.repeat("=", title.length()));
//                            System.out.println(comment);                                                                            
                        }
                    }.visit(StaticJavaParser.parse(file), null);
                } catch (Exception e) {
                    numberOfErrorFiles += 1;
                	System.out.println("Exception: " + e.getMessage() + " encountered while analyzing file: " + path);
                }
            }).explore(projectDir);    		
    	}
    	
    	if (commentType.equals("LineComment")) {
//           	System.out.println("LineComment: ");
    		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(LineComment comment, Object arg) {
                            super.visit(comment, arg);
                            String title = null;
                            if (parsedFilesMap.get(path) == null) {
                            	parsedFilesMap.put(path, 1);
                            } 
                            if (comment.getCommentedNode().isPresent()) {
                                title = String.format("%s (%s)", describe(comment.getCommentedNode().get()), path);
                                Optional<Node> parentNode = comment.getParentNode();
                                Optional<Node> commentedNode = comment.getCommentedNode();
                                String commentedNodeClass = commentedNode.get().getClass().toString();
                                //* print the name commented node, it can throw noSuchElementException                        
//                                System.out.println("comment's parent node class: " + commentedNode.get().getClass());                               
                                if (lineCommentsMap.get(commentedNodeClass) == null) {
                                	lineCommentsMap.put(commentedNodeClass, 0);
                                }
                                lineCommentsMap.put(commentedNodeClass, lineCommentsMap.get(commentedNodeClass) + 1);                            
                            } else {
                                title = String.format("No element associated (%s)", path);
                                if (lineCommentsMap.get("orphanComments") == null) {
                                	lineCommentsMap.put("orphanComments", 0);
                                }
                                lineCommentsMap.put("orphanComments", lineCommentsMap.get("orphanComments") + 1);  
                            }                              
                              //* printing the comment.
//                            System.out.println(title);
//                            System.out.println(Strings.repeat("=", title.length()));
//                            System.out.println(comment);                                                                            
                        }
                    }.visit(StaticJavaParser.parse(file), null);
                } catch (Exception e) {
                	numberOfErrorFiles += 1;
                	System.out.println("Exception: " + e.getMessage() + " encountered while analyzing file: " + path);
                }
            }).explore(projectDir);    
    	}
    	
    	if (commentType.equals("BlockComment")) {
//           	System.out.println("BlockComment: ");
    		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
                try {
                    new VoidVisitorAdapter<Object>() {
                        @Override
                        public void visit(BlockComment comment, Object arg) {
                            super.visit(comment, arg);
                            String title = null;
                            if (parsedFilesMap.get(path) == null) {
                            	parsedFilesMap.put(path, 1);
                            } 
                            if (comment.getCommentedNode().isPresent()) {
                                title = String.format("%s (%s)", describe(comment.getCommentedNode().get()), path);
                                Optional<Node> parentNode = comment.getParentNode();
                                Optional<Node> commentedNode = comment.getCommentedNode();
                                String commentedNodeClass = commentedNode.get().getClass().toString();
                                //* print the name commented node, it can throw noSuchElementException                        
//                                System.out.println("comment's parent node class: " + commentedNode.get().getClass());                               
                                if (blockCommentsMap.get(commentedNodeClass) == null) {
                                	blockCommentsMap.put(commentedNodeClass, 0);
                                }
                                blockCommentsMap.put(commentedNodeClass, blockCommentsMap.get(commentedNodeClass) + 1);                            
                            } else {
                                title = String.format("No element associated (%s)", path);
                                if (blockCommentsMap.get("orphanComments") == null) {
                                	blockCommentsMap.put("orphanComments", 0);
                                }
                                blockCommentsMap.put("orphanComments", blockCommentsMap.get("orphanComments") + 1);  
                            }                              
                              //* printing the comment.
//                            System.out.println(title);
//                            System.out.println(Strings.repeat("=", title.length()));
//                            System.out.println(comment);                                                                            
                        }
                    }.visit(StaticJavaParser.parse(file), null);
                } catch (Exception e) {
                	numberOfErrorFiles += 1;
                	System.out.println("Exception: " + e.getMessage() + " encountered while analyzing file: " + path);
                }
            }).explore(projectDir);   
    	}
    }

    private String describe(Node node) {
        if (node instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration)node;
            return "Method " + methodDeclaration.getDeclarationAsString();
        }
        if (node instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration)node;
            return "Constructor " + constructorDeclaration.getDeclarationAsString();
        }
        if (node instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration)node;
            if (classOrInterfaceDeclaration.isInterface()) {
                return "Interface " + classOrInterfaceDeclaration.getName();
            } else {
                return "Class " + classOrInterfaceDeclaration.getName();
            }
        }
        if (node instanceof EnumDeclaration) {
            EnumDeclaration enumDeclaration = (EnumDeclaration)node;
            return "Enum " + enumDeclaration.getName();
        }
        if (node instanceof FieldDeclaration) {
            FieldDeclaration fieldDeclaration = (FieldDeclaration)node;
            List<String> varNames = fieldDeclaration.getVariables().stream().map(v -> v.getName().getId()).collect(Collectors.toList());
            return "Field " + String.join(", ", varNames);
        }
        return node.toString();
    }
    
    public void printCommentMap() {
    	
    	System.out.println("counts of javaDocComments: ");
    	for (String commentNodeType: javaDocCommentsMap.keySet()){
            String key = commentNodeType.toString();
            int value = javaDocCommentsMap.get(key);  
            numberOfJavaDocComments += value;
            System.out.println(key + " : " + value);  
    	} 
    	
    	
    	System.out.println("counts of lineCommentsMap: ");
    	for (String commentNodeType: lineCommentsMap.keySet()){
            String key = commentNodeType.toString();
            int value = lineCommentsMap.get(key);  
            numberOfLineComments += value;
            System.out.println(key + " : " + value);  
    	} 
    	
    	
    	System.out.println("counts of javaDocComments: ");
    	for (String commentNodeType: blockCommentsMap.keySet()){
            String key = commentNodeType.toString();
            int value = blockCommentsMap.get(key);  
            numberofBlockCommments += value;
            System.out.println(key + " : " + value);  
    	} 
    	
    	totalNumberOfComments = numberofBlockCommments + numberOfJavaDocComments + numberOfLineComments;
    	
    	System.out.println("summary: ");
    	System.out.println("numberOfFilesParsed: " + parsedFilesMap.size());
    	System.out.println("numberOfFilesWhichErrored: " + numberOfErrorFiles);
    	System.out.println("numberOfJavaDocComments : " + numberOfJavaDocComments);
    	System.out.println(" numberOfLineComments : " + numberOfLineComments);
    	System.out.println("numberofBlockCommments : " + numberofBlockCommments);
    	System.out.println("totalNumberOfComments : " + totalNumberOfComments);
    }    

}