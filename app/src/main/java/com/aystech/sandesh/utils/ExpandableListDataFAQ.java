package com.aystech.sandesh.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataFAQ {

    public static HashMap<String, List<String>> getData() {
        String qusOne = "Can we change the pick-up address after an order has been placed?";
        String qusTwo = "Can we change the drop address after an order has been placed?";
        String qusThree = "What happens when I cancel my order?";
        String qusFour = "When will my parcel get delivered?";
        String qusFive = "Will I get a Proof of Delivery?";
        String qusSix = "In case the receiver is not there will you be retrying to deliver my parcel the next day?";
        String qusSeven = "In case your maximum attempts for delivery is expired what do I need to do next?";
        String qusEight = "Can I pay in cash?";
        String qusNine = "In case the parcel is in transit can I change the address of the receiver then?";
        String qusTen = "Can I send multiple documents/parcels in single order?";
        String qusEleven = "In case my parcel is lost how will I be compensated?";
        String qusTwelve = "Do you insure the Papers or parcels being sent?";
        String qusThirteen = "How do we identify the person who is either coming for delivery or pickup at my place?";
        String qusFourteen = "What is the weight limit for the delivery package?";
        String qusFifteen = "How long will the Traveler wait for the order pick up?";
        String qusSixteen = "On what days are you non-operational?";
        String qusSeventeen = "I am facing a technical/other issue. What should I do?";
        String qusEighteen = "Do you provide a different rate scale for corporate orders?";
        String qusNineteen = "I want to cancel a specific item from the order. Is it possible?";
        String qusTwenty = "I have placed multiple orders. Some of the orders are not ready, can I ask the delivery person to revisit?";
        String qusTwentyOne = "Will you pick up only within the time slot selected by me?";
        String qusTwentyTwo = "In case I don’t agree to the weight which has been changed by the service provider what do I need to do?";
        String qusTwentyThree = "In case of bulk order will I get discount?";
        String qusTwentyFour = "What happens when the receiver is not available at the time of delivery?";
        String qusTwentyFive = "How can I contact my pick-up and delivery personnel?";
        String qusTwentySix = "What can be shipped in the SANDESH Box? ";
        String qusTwentySeven = "What are the types of delivery services provided by SANDESH viz. whether Door to Door services are being provided?";
        String qusTwentyEight = "Are the charges different for different modes of delivery?";
        String qusTwentyNine = "How can I refer a friend and what is the benefit that I shall get by referring a friend?";

        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> oneAns = new ArrayList<>();
        oneAns.add("The Sender can change the pick-up address as long as the Traveler does not start journey. Till that time, the user may also change the terms of delivery from door to door to any other mode of delivery however with additional charge. Further you can cancel the order and create a new request with the changed address. (TNCs applied)");

        List<String> twoAns = new ArrayList<>();
        twoAns.add("Brazil");

        List<String> threeAns = new ArrayList<>();
        threeAns.add("In case you wish to cancel your order, you will not be charged a fee at least 6 prior to the Traveler starts journey in case of inter-city transaction and at least 2 hours before Traveler starts journey in case of intra-city transaction. If cancellation happens after that, 50% of order value will be deducted from your account.");

        List<String> fourAns = new ArrayList<>();
        fourAns.add("Subject to all conditions being normal, your parcels will be delivered the same day, the Traveler reaches the city of destination.");

        List<String> fiveAns = new ArrayList<>();
        fiveAns.add("Once your order has been delivered, the Traveler will upload a selfie with Receiver and parcel which will be a POD.");

        List<String> sixAns = new ArrayList<>();
        sixAns.add("NO, Normally you can track the movement of Traveler and can know the expected time of delivery and can plan accordingly. However, in case you are not available, you can collect the parcel from Traveler’s place at your convenient time.");

        List<String> sevenAns = new ArrayList<>();
        sevenAns.add("You will need to collect parcel from Traveler on your own.");

        List<String> eightAns = new ArrayList<>();
        eightAns.add("Sorry, currently we don’t accept cash.");

        List<String> nineAns = new ArrayList<>();
        nineAns.add("No, once the parcel is picked up no changes can be made in delivery address. But you can nominate alternate person to take delivery of goods till the Traveler reaches city of destination.");

        List<String> tenAns = new ArrayList<>();
        tenAns.add("No, you may opt for this by adding more orders in Send parcel menu.");

        List<String> elevenAns = new ArrayList<>();
        elevenAns.add("We would take utmost care not to lose your parcel but however in case the same is misplaced then our liability is limited to Rs. 50.");

        List<String> twelveAns = new ArrayList<>();
        twelveAns.add("It will be the sender’s responsibility to insure the Papers/Parcels.");

        List<String> thirteenAns = new ArrayList<>();
        thirteenAns.add("The Traveler will come with an OTP for the particular transaction based on which start journey can be made. With OTP, you can verify the person.");

        List<String> fourteenAns = new ArrayList<>();
        fourteenAns.add("Currently, we deliver a maximum of 20 kgs per request.");

        List<String> fifteenAns = new ArrayList<>();
        fifteenAns.add("The Traveler will wait for 15 mins, post which he will be eligible to cancel the order.");

        List<String> sixteenAns = new ArrayList<>();
        sixteenAns.add("We are operational on 24x7x365.");

        List<String> seventeenAns = new ArrayList<>();
        seventeenAns.add("Drop an email at Info@papersnparcels.com with your query and our team will get back to you at the earliest.");

        List<String> eighteenAns = new ArrayList<>();
        eighteenAns.add("We are offering rates @around 50% lower than the market rates in case of Corporate parcels. We are open subject to the terms and conditions of the company. Please drop an email at info@papersnparcels.com for more details.");

        List<String> nineteenAns = new ArrayList<>();
        nineteenAns.add("NO. Entire order needs to be cancelled and fresh order is to be booked.");

        List<String> twentyAns = new ArrayList<>();
        twentyAns.add("You have the option of selecting Different time for multiple orders.");

        List<String> twentyOneAns = new ArrayList<>();
        twentyOneAns.add("With all conditions remaining normal, your order would be picked in the time slot selected by you.");

        List<String> twentyTwoAns = new ArrayList<>();
        twentyTwoAns.add("The Traveler and Sender would normally check the apparent weight of the Consignments to fall in the category of Consignments based on weight as declared by the Sender. In case of any dispute with regard to the weight, the sender shall make arrangement for checking of the weight by arranging for weighing scale which would deem to be final. However, if the Sender is unable to arrange for the weighing scale, the weight declared by Traveler would be final and binding upon all the parties and Sender would be liable to pay the charges accordingly. If the Sender does not agree to the same then the Consignments would not be picked up. In such case, charge @50% of the applicable charges for Consignments in normal course would then be recovered from Sender towards compensation.");

        List<String> twentyThreeAns = new ArrayList<>();
        twentyThreeAns.add("Yes we would be open for further discussion subject to the terms and conditions of the company.");

        List<String> twentyFourAns = new ArrayList<>();
        twentyFourAns.add("Not to worry, Normally you can track the movement of Traveler and can know the expected time of delivery and can plan accordingly. However, in case you are not available, you can collect the parcel from Traveler’s place at your convenient time.");

        List<String> twentyFiveAns = new ArrayList<>();
        twentyFiveAns.add("You can send notification messages to the Traveler i.e. pick up and delivery personnel through the APP.");

        List<String> twentySixAns = new ArrayList<>();
        twentySixAns.add("The content in the box should not exceed the weight specification of the box. Contents should be compatible with the container and packed securely to ensure safe transportation with ordinary care in handling. Please refer to the list of restricted and banned items which cannot be transported as per IATA guidelines. For further reference View List. The final decision for nature of goods that can be shipped shall be taken by the TRAVELER.");

        List<String> twentySevenAns = new ArrayList<>();
        twentySevenAns.add("Yes. There are four types of delivery terms offered by SANDESH which is further offered by TRAVELER to the SENDER. The sender will be provided with following terms of delivery;");
        twentySevenAns.add("1.\tDoor to Door Service\n" +
                "Traveler shall pick up the Consignments from Sender’s Place and shall arrange for delivery of the Consignments at Receiver’s place as per schedule of the journey of the Traveler to city of destination. Further, Traveler shall deliver the consignment to the Receiver’s place within shortest possible time after reaching the city of destination but in any case within 6 hrs from the time of reaching city of destination. Traveler agrees and authorizes SANDESH to deduct penalty of Rs.5/- per hour from the amount payable to Traveler and also to recover any extra amount as arrear from future payments in case of delay in delivery beyond 6 hrs from the time of reaching city of destination.");
        twentySevenAns.add("2.\tSender’s place to Traveler’s place\n" +
                "Services made available on the APP shall enable users to arrange and schedule pick-up and delivery of documents and Consignments [together “Consignments”] within the region specified on APP from Sender’s place [“Pick-up Point”] to Traveler’s place [“Delivery Point”]. Consignments shall be picked up from the Pick-up Point by the Traveler as per mutually agreed terms between Sender and Traveler and the Consignments shall be available for delivery to Receiver at the Traveler’s place from the time of arrival of Traveler in the city of destination. The sender/Receiver shall arrange for taking delivery of the Consignments from traveler’s place at the earliest but in any case within 6 hours from the time of traveler reaching city of destination. Sender agrees and authorizes SANDESH to recover additional charge of Rs.10/- per hour from him/her and also to recover any extra amount as arrear in case of delay in taking delivery of Consignments beyond 6 hrs from the time of reaching city of destination.");
        twentySevenAns.add("3.\tTraveler’s place to Traveler’s place\n" +
                "Services made available on the APP shall enable users to arrange and schedule pick-up and delivery of documents and Consignments [together “Consignments”] within the region specified on APP from a designated point of pick-up [“Pick-up Point”] to a designated point of delivery [“Delivery Point”]. Sender shall arrange for handing over the Consignments to Traveler at the Pick-up Point as per mutually agreed terms between Sender and Traveler. The Sender shall ensure that the Consignments are handed over to the traveler in time but in any case at least 2 hour prior of start of journey for traveler. The Traveler shall have the authority to decide for the Consignments reaching within 2 hours from the start of journey.  The Consignments shall be available for delivery to Receiver at the Traveler’s place from the time of arrival of Traveler in the city of destination. The sender/Receiver shall arrange for taking delivery of the Consignments from traveler’s place at the earliest but in any case within 6 hours from the time of traveler reaching city of destination. Sender agrees and authorizes SANDESH to recover additional charge of Rs.10/- per hour from him/her and also to recover any extra amount as arrear in case of delay in taking delivery of Consignments beyond 6 hrs from the time of reaching city of destination.");
        twentySevenAns.add("4.\tTraveler’s place to Receiver’s place\n" +
                "Services made available on the APP shall enable users to arrange and schedule pick-up and delivery of documents and Consignments [together “Consignments”] within the region specified on APP from a designated point of pick-up [“Pick-up Point”] to a designated point of delivery [“Delivery Point”]. Sender shall arrange for handing over the Consignments to Traveler at the Pick-up Point as per mutually agreed terms between Sender and Traveler. The Sender shall ensure that the Consignments are handed over to the traveler in time but in any case at least 2 hour prior of start of journey for traveler. The Traveler shall have the authority to decide for the Consignments reaching within 2 hours from the start of journey.  Traveler shall deliver the consignment to the Receiver’s place within shortest possible time after reaching the city of destination but in any case within 6 hrs from the time of reaching city of destination. Traveler agrees and authorizes SANDESH to deduct penalty of Rs.5/- per hour from the amount payable to Traveler and also to recover any extra amount as arrear from future payments in case of delay in delivery beyond 6 hrs from the time of reaching city of destination.");
        List<String> twentyEightAns = new ArrayList<>();
        twentyEightAns.add("Yes. Considering the pick-up and delivery point involved, the Traveler needs to be paid different amounts for the services rendered. As such, the charges will be highest for “Door to Door Facility” and will be lowest for “Traveler’s Place to Traveler’s Place”.");

        List<String> twentyNineAns = new ArrayList<>();
        twentyNineAns.add("You can send a notification message to your friend by selecting the option “Refer a Friend”. By referring a friend, you will be entitled for a life time royalty in the form of a share of profit from the earnings out of the transaction made as sender i.e. amount earned from your friend.");

        expandableListDetail.put(qusOne, oneAns);
        expandableListDetail.put(qusTwo, twoAns);
        expandableListDetail.put(qusThree, threeAns);
        expandableListDetail.put(qusFour, fourAns);
        expandableListDetail.put(qusFive, fiveAns);
        expandableListDetail.put(qusSix, sixAns);
        expandableListDetail.put(qusSeven, sevenAns);
        expandableListDetail.put(qusEight, eightAns);
        expandableListDetail.put(qusNine, nineAns);
        expandableListDetail.put(qusTen, tenAns);
        expandableListDetail.put(qusEleven, elevenAns);
        expandableListDetail.put(qusTwelve, twelveAns);
        expandableListDetail.put(qusThirteen, thirteenAns);
        expandableListDetail.put(qusFourteen, fourteenAns);
        expandableListDetail.put(qusFifteen, fifteenAns);
        expandableListDetail.put(qusSixteen, sixteenAns);
        expandableListDetail.put(qusSeventeen, seventeenAns);
        expandableListDetail.put(qusEighteen, eighteenAns);
        expandableListDetail.put(qusNineteen, nineteenAns);
        expandableListDetail.put(qusTwenty, twentyAns);
        expandableListDetail.put(qusTwentyOne, twentyOneAns);
        expandableListDetail.put(qusTwentyTwo, twentyTwoAns);
        expandableListDetail.put(qusTwentyThree, twentyThreeAns);
        expandableListDetail.put(qusTwentyFour, twentyFourAns);
        expandableListDetail.put(qusTwentyFive, twentyFiveAns);
        expandableListDetail.put(qusTwentySix, twentySixAns);
        expandableListDetail.put(qusTwentySeven, twentySevenAns);
        expandableListDetail.put(qusTwentyEight, twentyEightAns);
        expandableListDetail.put(qusTwentyNine, twentyNineAns);
        return expandableListDetail;
    }
}
