package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AdsController {

    private static final Logger log = LoggerFactory.getLogger(AdsController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AdsController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PostMapping("/addAdsComments")
    public ResponseEntity<AdsCommentDto> addAdsCommentsUsingPOST(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.DEFAULT, description = "comment", required = true, schema = @Schema()) @Valid @RequestBody AdsCommentDto body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AdsCommentDto>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"author\" : 6,\n  \"pk\" : 1,\n  \"text\" : \"text\"\n}", AdsCommentDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AdsCommentDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/addAds")
    public ResponseEntity<AdsDto> addAdsUsingPOST(@Parameter(in = ParameterIn.DEFAULT, description = "createAds", required=true, schema=@Schema()) @Valid @RequestBody CreateAdsDto body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AdsDto>(objectMapper.readValue("{\n  \"image\" : \"image\",\n  \"author\" : 6,\n  \"price\" : 5,\n  \"pk\" : 1,\n  \"title\" : \"title\"\n}", AdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AdsDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/deleteAdsComment")
    public ResponseEntity<Void> deleteAdsCommentUsingDELETE(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getALLAds")
    public ResponseEntity<ResponseWrapperAdsDto> getALLAdsUsingGET() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ResponseWrapperAdsDto>(objectMapper.readValue("{\n  \"count\" : 0,\n  \"results\" : [ {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  }, {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  } ]\n}", ResponseWrapperAdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getAdsComment")
    public ResponseEntity<AdsCommentDto> getAdsCommentUsingGET(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AdsCommentDto>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"author\" : 6,\n  \"pk\" : 1,\n  \"text\" : \"text\"\n}", AdsCommentDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AdsCommentDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getAdsMe")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMeUsingGET(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "authenticated", required = false) Boolean authenticated, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "authorities[0].authority", required = false) String authorities0Authority, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "credentials", required = false) Object credentials, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "details", required = false) Object details, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "principal", required = false) Object principal) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ResponseWrapperAdsDto>(objectMapper.readValue("{\n  \"count\" : 0,\n  \"results\" : [ {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  }, {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  } ]\n}", ResponseWrapperAdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getAds")
    public ResponseEntity<FullAdsDto> getAdsUsingGET(@Parameter(in = ParameterIn.PATH, description = "id", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<FullAdsDto>(objectMapper.readValue("{\n  \"image\" : \"image\",\n  \"authorLastName\" : \"authorLastName\",\n  \"authorFirstName\" : \"authorFirstName\",\n  \"phone\" : \"phone\",\n  \"price\" : 6,\n  \"description\" : \"description\",\n  \"pk\" : 0,\n  \"title\" : \"title\",\n  \"email\" : \"email\"\n}", FullAdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<FullAdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<FullAdsDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/removeAds")
    public ResponseEntity<Void> removeAdsUsingDELETE(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/updateAdsComment")
    public ResponseEntity<AdsCommentDto> updateAdsCommentUsingPATCH(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id, @Parameter(in = ParameterIn.DEFAULT, description = "comment", required = true, schema = @Schema()) @Valid @RequestBody AdsCommentDto body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AdsCommentDto>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"author\" : 6,\n  \"pk\" : 1,\n  \"text\" : \"text\"\n}", AdsCommentDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AdsCommentDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/updateAds")
    public ResponseEntity<AdsDto> updateAdsUsingPATCH(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id, @Parameter(in = ParameterIn.DEFAULT, description = "ads", required = true, schema = @Schema()) @Valid @RequestBody AdsDto body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AdsDto>(objectMapper.readValue("{\n  \"image\" : \"image\",\n  \"author\" : 6,\n  \"price\" : 5,\n  \"pk\" : 1,\n  \"title\" : \"title\"\n}", AdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AdsDto>(HttpStatus.NOT_IMPLEMENTED);
    }

}
