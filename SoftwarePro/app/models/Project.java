package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

// Product entity managed by Ebean
@Entity
public class Project extends Model {

   // Fields
   // Annotate id as primary key
   @Id
   private Long id;

   @Constraints.Required
   private String name;

   // Project contains many products
   @ManyToMany(cascade=CascadeType.ALL)
   private List<Employee> emppro;

   // Default constructor
   public  Project() {
   }
			    
   public  Project(Long id, String name, List<Employee> emppro) {
      this.id = id;
      this.name = name;
      this.emppro = emppro;
   }
   public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public List<Employee> getemppro() {
    return emppro;
}

public void setemppro (List<Employee> emppro) {
    this.emppro = emppro;
}
   //Generic query helper for entity Computer with id Long
public static Finder<Long,Project> find = new Finder<Long,Project>(Project.class);

//Find all Products in the database
public static List<Project> findAll() {
   return Project.find.query().where().orderBy("name asc").findList();
}

public static Map<String,String> options() {
    LinkedHashMap<String,String> options = new LinkedHashMap();
 
    // Get all the categories from the database and add them to the options hash map
    for (Project p: Project.findAll()) {
       options.put(p.getId().toString(), p.getName());
    }
    return options;
 }

 public static boolean inProject(Long project, Long emppro) {
    return find.query().where().eq("emppro.id", emppro)
                       .eq("id", project)
                       .findList().size() > 0;
}
}