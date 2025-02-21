console.log("Script Loaded");

let currentTheme=getTheme();

changeTheme(currentTheme);

function changeTheme(){
    
    console.log(currentTheme);
    //set to webPage
    document.querySelector("html").classList.add(currentTheme);

    //set the Listner to change theme button
    const changeThemeButton= document.querySelector('#theme_change_button');
    
    //change text of button
   
    changeThemeButton.querySelector("span").textContent=currentTheme=="light"?"Dark":"light";

    changeThemeButton.addEventListener("click",(event)=>{

       const oldTheme=currentTheme;
        if(currentTheme=="dark"){
            currentTheme="light";
           
        }else{
            currentTheme="dark";
        }
        //update in local Strorage
        setTheme(currentTheme);

        document.querySelector("html").classList.remove(oldTheme);

        document.querySelector("html").classList.add(currentTheme);
       
    });

    
}


//set theme to local Storage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}

function getTheme(){

    let theme=localStorage.getItem("theme");
    if(theme){
        return theme;
    }
    else return "light"; 
}
