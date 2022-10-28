package pro.sky.adsplatform.dto;
import lombok.Data;

/**
 * FullAds
 */
@Data
public class FullAds   {
  private String authorFirstName;
  private String authorLastName ;
  private String description;
  private String email;
  private String image;
  private String phone;
  private Integer pk;
  private Integer price;
  private String title;

}
