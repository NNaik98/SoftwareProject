package controllers;

//imports for image upload
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
//imports for image scaling
import java.io.IOException;
import java.awt.image.*;
import javax.imageio.*;
import org.imgscalr.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private FormFactory formFactory;
    private Environment e;

    @Inject
    public HomeController(FormFactory f,Environment env) {
        this.formFactory = f;
        this.e = env;
}
   
    public Result employees(Long cat) {
        List<Employee> empList = null;
        List<Project> projectList = Project.findAll();
        List<Department> departmentList = Department.findAll();
        
        if(cat ==0){
            empList = Employee.findAll();
        }else {
            empList = Project.find.ref(cat).getemppro();
            empList = Department.find.ref(cat).getEmp();
            
            
           
        }
        return ok(employees.render(empList, departmentList,
        User.getUserById(session().get("email")),e,projectList));

     }


    public Result home() {
        return ok(home.render(User.getUserById(session().get("email"))));
    }

   
    //ADDEMPLOYEE
    @Security.Authenticated(Secured.class)
    public Result addEmp() {
        Form<Address> AddressForm = formFactory.form(Address.class);
       
        Form<Employee> employeeForm = formFactory.form(Employee.class);
        return ok(addEmp.render(employeeForm,AddressForm,User.getUserById(session().get("email"))));
}


//Add and  Submit
@Security.Authenticated(Secured.class)
@Transactional
public Result addEmpSubmit() {
   
    Form<Employee> employeeForm = formFactory.form(Employee.class).bindFromRequest();
    Form<Address> AddressForm = formFactory.form(Address.class).bindFromRequest();
    
    if (employeeForm.hasErrors()) {
            return badRequest(addEmp.render(employeeForm,AddressForm,User.getUserById(session().get("email"))));
    } else {
          Employee newEmp = employeeForm.get();
          Address Address = AddressForm.get();
        

        List<Project> newCats = new ArrayList<Project>();
        for (Long cat : newEmp.getProSelect()) {
            newCats.add(Project.find.byId(cat));
        }
       
        newEmp.setProject (newCats);
        newEmp.setAddress(Address);


        if(newEmp.getId()==null){
            newEmp.save();
        }else{
            newEmp.update();
        }
        MultipartFormData<File> data = request().body().asMultipartFormData();
         FilePart<File> image = data.getFile("upload");
 
        String saveImageMessage = saveFile(newEmp.getId(), image);

         flash("success", "" + newEmp.getFname() + " " + newEmp.getLname() + "was added/updated." +saveImageMessage);
         return redirect(controllers.routes.HomeController.employees(0));
    }
}

public String saveFile(Long id, FilePart<File> uploaded) {
    // Make sure that the file exists.
    if (uploaded != null) {
        // Make sure that the content is actually an image.
        String mimeType = uploaded.getContentType();
        if (mimeType.startsWith("image/")) {
            // Get the file name.
            String fileName = uploaded.getFilename();
            // Extract the extension from the file name.
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i >= 0) {
                extension = fileName.substring(i + 1);
            }
        
            File file = uploaded.getFile();
          
            File dir = new File("public/images/EmployeeImages");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 3) Actually save the file.
            File newFile = new File("public/images/EmployeeImages/", id + "." + extension);
            if (file.renameTo(newFile)) {
                try {
                    BufferedImage img = ImageIO.read(newFile); 
                    BufferedImage scaledImg = Scalr.resize(img, 90);
                    
                    if (ImageIO.write(scaledImg, extension, new File("public/images/EmployeeImages/", id + "thumb.jpg"))) {
                        return "/ file uploaded and thumbnail created.";
                    } else {
                        return "/ file uploaded but thumbnail creation failed.";
                    }
                } catch (IOException e) {
                    return "/ file uploaded but thumbnail creation failed.";
                }
            } else {
                return "/ file upload failed.";
            }

        }
    }
    return "/ no image file.";
}

          

@Security.Authenticated(Secured.class)
@Transactional
@With(AuthAdmin.class)
public Result deleteEmp(Long id) {

    
    Employee.find.ref(id).delete();

   
    flash("success", "Employee has been deleted.");
    // And redirect to the onsale page
    return redirect(controllers.routes.HomeController.employees(0));
}

@Security.Authenticated(Secured.class)
@Transactional
public Result updateEmp(Long id) {
    Employee i;
    Address a;
    Form<Employee> employeeForm;
    Form<Address> AddressForm;

    try {
       
        i = Employee.find.byId(id);
        i.update();

        a=i.getAddress();
        
        employeeForm = formFactory.form(Employee.class).fill(i);
        AddressForm = formFactory.form(Address.class).fill(a);

    } catch (Exception ex) {
        return badRequest("error");
    }

    return ok(addEmp.render(employeeForm,AddressForm,User.getUserById(session().get("email"))));
}
@Security.Authenticated(Secured.class)
@Transactional
@With(AuthAdmin.class)
public Result deleteUser(String email) {

    // The following line of code finds the item object by id, then calls the delete() method
    // on it to have it removed from the database.

        User u = User.getUserById(email);
        u.delete();

    // Now write to the flash scope, as we did for the successful item creation.
    flash("success", "User has been deleted.");
    // And redirect to the onsale page
    return redirect(controllers.routes.HomeController.users());
}
@Security.Authenticated(Secured.class)
public Result updateUser(String email) {
    User u;
    Form<User> userForm;

    try {
        // Find the item by email
        u = User.getUserById(email);
        u.update();

        // Populate the form object with data from the user found in the database
        userForm = formFactory.form(User.class).fill(u);
    } catch (Exception ex) {
        return badRequest("error");
    }

    // Display the "add item" page, to allow the user to update the item
    return ok(addUser.render(userForm,User.getUserById(session().get("email"))));
}

@Security.Authenticated(Secured.class)
public Result addUser() {
    Form<User> userForm = formFactory.form(User.class);
    return ok(addUser.render(userForm,User.getUserById(session().get("email"))));
}
@Security.Authenticated(Secured.class)
@Transactional
public Result addUserSubmit() {

Form<User> newUserForm = formFactory.form(User.class).bindFromRequest();

if (newUserForm.hasErrors()) {
  
    return badRequest(addUser.render(newUserForm,User.getUserById(session().get("email"))));
} else {

    User newUser = newUserForm.get();
    
    if(User.getUserById(newUser.getEmail())==null){
        newUser.save();
    }else{
        newUser.update();
    }
    flash("success", "User " + newUser.getName() + " was added/updated.");
    return redirect(controllers.routes.HomeController.users()); 
    }
}

public Result users() {
    List<User> userList = null;

    userList = User.findAll();

    return ok(users.render(userList,User.getUserById(session().get("email"))));

 }

}
