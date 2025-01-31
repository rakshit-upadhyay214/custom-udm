import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityValue
import org.moqui.entity.EntityList
import org.moqui.service.ServiceFacade

ExecutionContext ec= context.ec

if(context.infoString){
    Map<Object, Object> existingCustomer=ec.service.sync().name("PartyService.find#FindCustomerView").parameters(context).call()
    if(existingCustomer){

        List<String> partyIdList = existingCustomer.get("partyList")

        String partyId= partyIdList.get(0)
        if(context.address1){
            EntityValue existingPcm = ec.entity.ef.condition(ec.entity.conditionFactory.makeCondition(
                    (ec.entity.conditionFactory.makeCondition("partyId",partyId)),
                    AND,
                    (ec.entity.conditionFactory.makeCondition("contactMechPurposeId","PostalAddress"))
            ))

            String contactMechId = existingPcm.contactMechId

            EntityValue existingPostalRecord = ec.entity.find("custom.udm.PostalAddress").condition("contactMechId",contactMechId)
        }




    }

}

