package ott_app;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class MovieObject implements Serializable
{
	private String trailer,descreption,writer, director, casts,image;
	private boolean isshow;
    public MovieObject(String image,String trailer,String descreption,String writer,String director,String casts,boolean isshow)
    {
    	this.image=image;
     	this.trailer=trailer;
    	this.descreption=descreption;
    	this.writer=writer;
    	this.director=director;
    	this.casts=casts;
    	this.isshow=isshow;
    }
   public String getImage()
   {
	return image;
   }
   public String getTrailer()
   {
	   return(trailer);
   }
   public String getDescription()
   {
	   return(descreption);
   }
   public String getWriter()
   {
	   return(writer);
   }
   public String getDirector()
   {
	   return(director);
   }
   public String getCasts()
   {
	   return(casts);
   }
   public boolean getShow()
   {
	   return(isshow);
   }
}
