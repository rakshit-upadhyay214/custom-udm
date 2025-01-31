import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityValue
import org.moqui.entity.EntityCondition

import org.moqui.entity.EntityList
import org.moqui.service.ServiceFacade

import java.sql.Timestamp

ExecutionContext ec= context.ec

Map<Object, Object> existingCustomer=ec.service.sync().name("PartyService.find#FindCustomerView").parameters(context).call()
if(existingCustomer){
    List<String> partyIdList = existingCustomer.get("partyList")
    ec.logger.debug("-----------------PartyIdList = ${partyIdList}------------------")
    String partyId= partyIdList.get(0)

    ec.logger.debug("-----------------PartyId = ${partyId}------------------")

    EntityFind relatedPcm = ec.entity.find("custom.udm.PartyContactMech").condition("partyId",partyId)

    EntityFind validPcm = relatedPcm.condition(
                            ec.entity.conditionFactory.makeCondition(
                            ec.entity.conditionFactory.makeCondition("thruDate", EntityCondition.EQUALS, null),
                            EntityCondition.OR,
                            ec.entity.conditionFactory.makeCondition("thruDate", EntityCondition.GREATER_THAN, new Timestamp(new Date().getTime()))))

    EntityList listOfValidPcm = validPcm.list()


    if(address1 || city || postalCode){
        for( EntityValue pcmRecord in listOfValidPcm ){
            if(pcmRecord.get("contactMechPurposeId")=="PostalAddress"){
                EntityValue updatedPcm = ec.entity.makeValue("custom.udm.PartyContactMech")
                        .set("partyId",partyId)
                        .set("contactMechId",pcmRecord.get("contactMechId"))
                        .set("contactMechPurposeId","PostalAddress")
                        .set("fromDate",pcmRecord.get("fromDate"))
                        .set("thruDate",new Timestamp(new Date().getTime())).update()
            }
        }
        EntityValue newContactMech = ec.entity.makeValue("custom.udm.ContactMech")
                .setSequencedIdPrimary()
                .set("contactMechTypeEnumId", "PostalAddress")
                .set("infoString",null)
                .create()

        String contactMechId = newContactMech.get("contactMechId")

        EntityValue newPostalCode = ec.entity.makeValue("custom.udm.PostalAddress")
                                        .set("contactMechId",contactMechId)
                                        .set("toName",firstName)
                                        .set("address1",address1)
                                        .set("city",city)
                                        .set("postalCode", postalCode)
                                        .create()

        EntityValue newPcmRecord = ec.entity.makeValue("custom.udm.PartyContactMech")
                                        .set("partyId",partyId)
                                        .set("contactMechId", contactMechId)
                                        .set("contactMechPurposeId", "PostalAddress")
                                        .set("fromDate",new Timestamp(new Date().getTime()))
                                        .set("thruDate",null)
                                        .create()
    }

    if(contactNumber){
        for( EntityValue pcmRecord in listOfValidPcm ){
            if(pcmRecord.get("contactMechPurposeId")=="TelecomNumber"){
                EntityValue updatedPcm = ec.entity.makeValue("custom.udm.PartyContactMech")
                        .set("partyId",partyId)
                        .set("contactMechId",pcmRecord.get("contactMechId"))
                        .set("contactMechPurposeId","TelecomNumber")
                        .set("fromDate",pcmRecord.get("fromDate"))
                        .set("thruDate",new Timestamp(new Date().getTime())).update()
            }
        }
        EntityValue newContactMech = ec.entity.makeValue("custom.udm.ContactMech")
                .setSequencedIdPrimary()
                .set("contactMechTypeEnumId", "TelecomNumber")
                .set("infoString",null)
                .create()

        String contactMechId = newContactMech.get("contactMechId")

        EntityValue newTelecom = ec.entity.makeValue("custom.udm.TelecomNumber")
                .set("contactMechId",contactMechId)
                .set("contactNumber",contactNumber)
                .create()
    }
}



