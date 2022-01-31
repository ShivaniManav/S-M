DELIMITER $$

CREATE PROCEDURE updateDefaultAddress(
		IN addr_id INT(11), 
		IN user_id INT(11)
	) 
BEGIN

	DECLARE temp_addr_id INT(11);
	DECLARE finished INT(1) DEFAULT 0;
	
	DECLARE user_addresses 
		CURSOR FOR 
			SELECT id FROM `user_address` WHERE `fk_user_id`=user_id AND `id`!=addr_id;
		
	DECLARE CONTINUE HANDLER
	FOR NOT FOUND SET finished=1;
	
	UPDATE `user_address`
	SET `is_default`=1
	WHERE `fk_user_id`=user_id AND `id`=addr_id;
	
	OPEN user_addresses;

		update_addrs: LOOP
				
			FETCH user_addresses INTO temp_addr_id;
			
			IF finished=1 THEN
				LEAVE update_addrs;
			END IF;
			
			UPDATE `user_address`
			SET `is_default`=0
			WHERE `id`=temp_addr_id;
			
		END LOOP update_addrs;
		
	CLOSE user_addresses;

END$$

DELIMITER ;