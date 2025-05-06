package es.gobcan.istac.coetl.service.validator;

import org.springframework.stereotype.Component;
import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;

@Component
public class FileValidator extends AbstractValidator<File> {

	 private static final String WRONG_FORMAT_ERROR_MESSAGE = "Format \"%s\" is not supported";
	 private static final String FILE_SIZE_EXCEEDED_MESSAGE = "File size is larger than %d";
	 private static final Long FILE_SIZE = (long) 52428800;
	
	@Override
	public void validate(File entity) {
		checkFileFormatIsValid(entity);
		checkFileSize(entity);
	}
	
	private void checkFileSize(File file){
		long fileSize;
		try {
			fileSize = file.getContent().length();
			if (fileSize > FILE_SIZE) {
				throw new Exception("File exceeded max size");
			}
		} catch (Exception e) {
			throw new CustomParameterizedExceptionBuilder().message(String.format(FILE_SIZE_EXCEEDED_MESSAGE, FILE_SIZE)).code(ErrorConstants.PARAMETER_FILE_SIZE_EXCEEDED).build();
		}
		
	}
	
	private void checkFileFormatIsValid(File file) {
		int lastDot= file.getName().lastIndexOf(".");
		String extension = file.getName().substring(lastDot+1).toUpperCase();
		try {
			// If there is no match, an error is thrown
			File.SupportedFormats.valueOf(extension);
	    } catch (IllegalArgumentException e) {
	    	throw new CustomParameterizedExceptionBuilder().message(String.format(WRONG_FORMAT_ERROR_MESSAGE, extension)).code(ErrorConstants.PARAMETER_WRONG_FILE_FORMAT).build();
	    }
    }
}
