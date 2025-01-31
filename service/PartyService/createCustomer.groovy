import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityValue
import org.moqui.entity.EntityList
import org.moqui.service.ServiceFacade
import java.sql.Timestamp

ExecutionContext ec = context.ec

Map<Object,Object> existingCustomer = ec.service
                                        .sync()
                                        .name("PartyService.find#FindCustomerView")
                                        .parameters(context)
                                        .call()

if(existingCustomer.get("listCount")>0){
    return null;
}

EntityValue ev= ec.entity
        .makeValue("Party")
        .set("partyTypeEnumId","PERSON")
        .setSequencedIdPrimary()
        .create()

partyId=ev.partyId

ev = ec.entity
        .makeValue("Person")
        .setAll(context)
        .create()

ev = ec.entity.makeValue("PartyRole").setAll(context).set("roleTypeId", "CUSTOMER").create()

ev = ec.entity.makeValue("ContactMech")
        .set("contactMechTypeEnumId", "EmailAddress")
        .setAll(context)
        .setSequencedIdPrimary()
        .create()

contactId = ev.contactMechId

ev = ec.entity.makeValue("PartyContactMech")
        .set("partyId",partyId)
        .set("contactMechId",contactId)
        .set("contactMechPurposeId","PrimaryEmail")
        .set("fromDate", new Timestamp(new Date().getDate()))
        .set("thruDate", null)
        .create()


context.listCount=existingCustomer.get(listCount)