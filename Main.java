import java.io.*;
import java.util.*;

class Person {
    String name;
    String gender; // "male" or "female"
    List<Person> parents;
    List<Person> spouses;
    List<Person> siblings;
    List<Person> grandparents;
    List<Person> greatGrandparents;
    List<Person> unclesAunts;
    List<Person> niecesNephews;
    List<Person> children; 

    Person(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.parents = new ArrayList<>();
        this.spouses = new ArrayList<>();
        this.siblings = new ArrayList<>();
        this.grandparents = new ArrayList<>();
        this.greatGrandparents = new ArrayList<>();
        this.unclesAunts = new ArrayList<>();
        this.niecesNephews = new ArrayList<>();
        this.children = new ArrayList<>(); 
    }
}

class FamilyTree {

    private Map<String, Person> people = new HashMap<>();

    public FamilyTree() {
        loadFromCSV("family_tree.csv");
    }

    private void loadFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            List<String[]> data = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim whitespace
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue; // Skip lines that don't have at least name and gender
                }

                data.add(parts);
                String name = parts[0];
                String gender = parts[1];

                if (!people.containsKey(name)) {
                    people.put(name, new Person(name, gender));
                }
            }

            for (String[] parts : data) {
                String name = parts[0];
                String parentNames = parts.length > 2 ? parts[2] : "";
                String spouseNames = parts.length > 3 ? parts[3] : "";
                String siblingNames = parts.length > 4 ? parts[4] : "";
                String grandparentNames = parts.length > 5 ? parts[5] : "";
                String greatGrandparentNames = parts.length > 6 ? parts[6] : "";
                String uncleAuntNames = parts.length > 7 ? parts[7] : "";
                String nieceNephewNames = parts.length > 8 ? parts[8] : "";

                Person person = people.get(name);

                // Handle relationships
                handleRelationships(person, parentNames, spouseNames, siblingNames, grandparentNames, greatGrandparentNames, uncleAuntNames, nieceNephewNames);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRelationships(Person person, String parentNames, String spouseNames, String siblingNames, String grandparentNames, String greatGrandparentNames, String uncleAuntNames, String nieceNephewNames) {
        // Handle parents
        if (!parentNames.isEmpty()) {
            String[] parents = parentNames.split(";");
            for (String parentName : parents) {
                Person parent = people.get(parentName);
                if (parent != null && !person.parents.contains(parent)) { // Avoid duplicates
                    person.parents.add(parent);
                    if (!parent.children.contains(person)) { // Avoid duplicates
                        parent.children.add(person);
                    }
                }
            }
        }
    
        // Handle spouses
        if (!spouseNames.isEmpty()) {
            String[] spouses = spouseNames.split(";");
            for (String spouseName : spouses) {
                Person spouse = people.get(spouseName);
                if (spouse != null && !person.spouses.contains(spouse)) { // Avoid duplicates
                    person.spouses.add(spouse);
                    if (!spouse.spouses.contains(person)) { // Avoid duplicates
                        spouse.spouses.add(person);
                    }
                }
            }
        }
    
        // Handle siblings
        if (!siblingNames.isEmpty()) {
            String[] siblings = siblingNames.split(";");
            for (String siblingName : siblings) {
                Person sibling = people.get(siblingName);
                if (sibling != null && !person.siblings.contains(sibling)) { // Avoid duplicates
                    person.siblings.add(sibling);
                    if (!sibling.siblings.contains(person)) { // Avoid duplicates
                        sibling.siblings.add(person);
                    }
                }
            }
        }
    
        // Handle grandparents
        if (!grandparentNames.isEmpty()) {
            String[] grandparents = grandparentNames.split(";");
            for (String grandparentName : grandparents) {
                Person grandparent = people.get(grandparentName);
                if (grandparent != null && !person.grandparents.contains(grandparent)) { // Avoid duplicates
                    person.grandparents.add(grandparent);
                }
            }
        }
    
        // Handle great-grandparents
        if (!greatGrandparentNames.isEmpty()) {
            String[] greatGrandparents = greatGrandparentNames.split(";");
            for (String greatGrandparentName : greatGrandparents) {
                Person greatGrandparent = people.get(greatGrandparentName);
                if (greatGrandparent != null && !person.greatGrandparents.contains(greatGrandparent)) { // Avoid duplicates
                    person.greatGrandparents.add(greatGrandparent);
                }
            }
        }
    
        // Handle uncles/aunts
        if (!uncleAuntNames.isEmpty()) {
            String[] unclesAunts = uncleAuntNames.split(";");
            for (String uncleAuntName : unclesAunts) {
                Person uncleAunt = people.get(uncleAuntName);
                if (uncleAunt != null && !person.unclesAunts.contains(uncleAunt)) { // Avoid duplicates
                    person.unclesAunts.add(uncleAunt);
                }
            }
        }
    
        // Handle nieces/nephews
        if (!nieceNephewNames.isEmpty()) {
            String[] niecesNephews = nieceNephewNames.split(";");
            for (String nieceNephewName : niecesNephews) {
                Person nieceNephew = people.get(nieceNephewName);
                if (nieceNephew != null && !person.niecesNephews.contains(nieceNephew)) { // Avoid duplicates
                    person.niecesNephews.add(nieceNephew);
                }
            }
        }
    }
    

    public void addPerson(String name, String gender, List<String> parentNames, List<String> spouseNames, List<String> siblingNames, List<String> grandparentNames, List<String> greatGrandparentNames, List<String> uncleAuntNames, List<String> nieceNephewNames) {
        Person person = new Person(name, gender);
        people.put(name, person); 

        // Add relationships
        handleRelationships(person, String.join(";", parentNames), String.join(";", spouseNames), String.join(";", siblingNames), String.join(";", grandparentNames), String.join(";", greatGrandparentNames), String.join(";", uncleAuntNames), String.join(";", nieceNephewNames));

        saveToCSV("family_tree.csv");
    }

    private void saveToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("name,gender,parents,spouses,siblings,grandparents,greatGrandparents,unclesAunts,niecesNephews\n");
            for (Person person : people.values()) {
                String parentNames = String.join(";", person.parents.stream().map(p -> p.name).toArray(String[]::new));
                String spouseNames = String.join(";", person.spouses.stream().map(s -> s.name).toArray(String[]::new));
                String siblingNames = String.join(";", person.siblings.stream().map(s -> s.name).toArray(String[]::new));
                String grandparentNames = String.join(";", person.grandparents.stream().map(g -> g.name).toArray(String[]::new));
                String greatGrandparentNames = String.join(";", person.greatGrandparents.stream().map(g -> g.name).toArray(String[]::new));
                String uncleAuntNames = String.join(";", person.unclesAunts.stream().map(u -> u.name).toArray(String[]::new));
                String nieceNephewNames = String.join(";", person.niecesNephews.stream().map(n -> n.name).toArray(String[]::new));
                bw.write(person.name + "," + person.gender + "," + parentNames + "," + spouseNames + "," + siblingNames + "," + grandparentNames + "," + greatGrandparentNames + "," + uncleAuntNames + "," + nieceNephewNames + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public String findRelation(String name1, String name2) {
        if (!people.containsKey(name1) || !people.containsKey(name2)) {
            return "One or both persons not found in the family tree.";
        }

        Person person1 = people.get(name1);
        Person person2 = people.get(name2);

        if (person1 == person2) {
            return "self";
        }

        // Checking for direct relationships
        if (person1.spouses.contains(person2)) {
            if(person1.gender.equals("male"))
            {
                return("Pati(husband)");
            }
            return("Patni(wife)");
        }

        if (person1.parents.contains(person2)) {
            if(person2.gender.equals("male"))
            {
                return("Beta(son)");
            }
            return "Beti(daughter)";
        }

        if (person2.parents.contains(person1)) {
            if(person1.gender.equals("male"))
            {
                return("Pita(father)");
            }
            return "Mata(mother)";
        }

        // Checking siblings
        if (areSiblings(person1, person2)) {
            return person1.gender.equals("male") ? "Bhai(brother)" : "Behan(sister)";
        }

        if(isUncleOrAunt(person1,person2))
        {
            Person parM = null;
            Person parF = null;
            for(Person par : person2.parents)
            {
                if(par.gender.equals("male"))
                {
                    parF = par;
                }
                else
                parM = par;
            }
            if(parF != null &&areBrotherOrSister(person1,parF))
            {
                if(isBrotherOf(parF,person1))
                {
                    return "Chacha";
                }
                if(isSisterOf(parF,person1))
                {
                    return "Bua";
                }
            }
            if(parF != null && isBrothersWife(parF,person1))
            {
                return "Chachi";
            }
            if(parF != null && isSistersHusband(parF,person1))
            {
                return "Mamaji(bua's husband)";
            }
            if(parM != null && areBrotherOrSister(person1,parM))
            {
                if(isBrotherOf(parM,person1))
                {
                    return "Mama";
                }
                if(isSisterOf(parM,person1))
                {
                    return "Mousi";
                }
            }
            if(parM != null && isBrothersWife(parM,person1))
            {
                return "Mami";
            }
            if(parM != null && isSistersHusband(parM,person1))
            {
                return "Mousa";
            }
        }
        if(isNanaOrNani(person1, person2))
        {
            return person1.gender.equals("male") ? "Nana" : "Nani";
        }
        if(isBhaiyaOrBhabhi(person1, person2))
        {
            return person1.gender.equals("male") ? "Bhaiya" : "Bhabhi";
        }

        // Checking nephew/niece
        if (isNephewOrNiece(person1, person2)) {
            return person1.gender.equals("male") ? "Bhatija(nephew)" : "Bhatiji(niece)";
        }

        // Checking brother/sister-in-law
        if (isBrotherorSisterInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Sala(brother-in-law)" : "sali(sister-in-law";
        }

        if (isParentInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Sasur(father-in-law)" : "Saas(mother-in-law)";
        }

        if (isChildInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Damaad(son-in-law)" : "Putravadhu(daughter-in-law)";
        }

        if (isGrandchildInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Parpota(Grandson-in-law)" : "Parpoti(Granddaughter-in-law)";
        }

        if (isGrandparentInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Pardada(Grandfather-in-law)" : "Pardadi(Grandmother-in-law)";
        }

        if (isGreatGrandparentInLaw(person1, person2)) {
            return person1.gender.equals("male") ? "Pardada(great-grandfather-in-law)" : "Pardadi(great-grandmother-in-law)";
        }

        // Checking grandchild
        if (isGrandchild(person1, person2)) {
            if(person1.gender.equals("male"))
            {
                return("Pota(grandson)");
            }
            return "Poti(grand-daughter)";
        }

        // Checking great-grandparent
        if (isGreatGrandparent(person1, person2)) {
            if(person1.gender.equals("male"))
            {
                return("Par-dada(Great-GrandFather)");
            }
            return "Par-dadi(Great-GrandMother)";
        }

        // Checking great-grandchild
        if (isGreatGrandchild(person1, person2)) {
            if(person2.gender.equals("male"))
            {
                return("Par-pota(Great-GrandSon)");
            }
            return "Par-poti(Great-GrandDaughter)";
        }

        // Checking son/daughter
        if (isSonOrDaughter(person1, person2)) {
            return person1.gender.equals("male") ? "Beta(son)" : "Beti(daughter)";
        }

        // Checking grandfather/grandmother
        if (isGrandfatherOrGrandmother(person1, person2)) {
            return person1.gender.equals("male") ? "Dada(grandfather)" : "Dadi(grandmother)";
        }

        // Checking great-grandfather/great-grandmother
        if (isGreatGrandfatherOrGreatGrandmother(person1, person2)) {
            return person1.gender.equals("male") ? "Par-Dada(great-grandfather)" : "Par-Dadi(great-grandmother)";
        }

        // Checking granduncle/grandaunt
        if (isGranduncleOrGrandaunt(person1, person2)) {
            return person1.gender.equals("male") ? "granduncle" : "grandaunt";
        }

        // Checking great-granduncle/great-grandaunt
        if (isGreatGranduncleOrGreatGrandaunt(person1, person2)) {
            return person1.gender.equals("male") ? "great-granduncle" : "great-grandaunt";
        }

        return "unknown";
    }

    // Helper methods for relationship checks

    private boolean isUncleOrAunt(Person person1, Person person2) {
        for (Person sib : person1.siblings) {
            if (isParent(person2,sib)) {
                return true;
            }
        }
        for(Person spous : person1.spouses)
        {
            for (Person si : spous.siblings) {
            if (isParent(person2,si)) {
                return true;
            }
        }}
        for(Person sib : person1.siblings)
        {

            if(isParent(sib,person2)){
                return true;
            }
        }
        for(Person sp : person1.spouses)
        {
            for(Person sib : sp.siblings)
            {
    
                if(isParent(sib,person2)){
                    return true;
                }
            } 
        }
        return false;
    }//done
  
    private boolean isNanaOrNani(Person grandparent, Person child) {
        for (Person parent : grandparent.children) {
            if ("female".equals(parent.gender) && isParent(child, parent)) {
                return true;
            }
        }
    
        // Check spouse's children, excluding already checked biological children
        for (Person spouse : grandparent.spouses) {
            for (Person parent : spouse.children) {
                if (!grandparent.children.contains(parent) && isParent(child, parent) && "female".equals(parent.gender)) {
                    return true;
                }
            }
        }

        return false;
    }
    private boolean isBhaiyaOrBhabhi(Person person1, Person person2) {
        for(Person par : person1.parents)
        {
            for(Person sib :par.siblings)
            {
                if(isParent(person2,sib))
                {
                    return true;
                }
            }
        }
        for(Person sp : person1.spouses)
        {
            for(Person par : sp.parents)
            {
                for(Person sib :par.siblings)
                {
                    if(isParent(person2,sib))
                    {
                        return true;
                    }
                }
            }
        }    // Recursive case
        for (Person child : person2.parents) {
            if (isBhaiyaOrBhabhi(person1, child)) {
                return true;
            }
        }
        return false;
    }
    
    
    // private boolean isGrandchild(Person person1, Person person2) {
    //     for (Person parent : person2.children) {
    //         if (parent.children.contains(person1)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }  //done

    private boolean isGrandchild(Person person1, Person person2) {
        for (Person par : person2.children) {
            // Check if the child is the father of person1
            if ("Male".equals(par.gender) && par.children.contains(person1)) {
                return true;
            }
        }
        return false;
    }
    

    private boolean isParent(Person person1, Person person2) {
        return person1.parents.contains(person2);
    }// done but remember this takes input in opposite way not in correct way

    private boolean isGrandparentInLaw(Person person1, Person person2) {
        // Check if person1 is the grandparent of any spouse of person2
        for (Person spouse : person2.spouses) {
            if (isGrandparent(person1, spouse)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isGreatGrandparent(Person person1, Person person2) {
        for (Person parent : person2.parents) {
            if (isGrandchild(parent, person1)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isBrotherOf(Person person1, Person person2) {
        for (Person sibling : person1.siblings) {
            if (sibling.equals(person2) && sibling.gender.equals("male")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSisterOf(Person person1, Person person2) {
        for (Person sibling : person1.siblings) {
            if (sibling.equals(person2) && sibling.gender.equals("female")) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isBrothersWife(Person person1, Person person2) {
        for (Person sibling : person1.siblings) {
            if (sibling.gender.equals("male")) { // Check if sibling is male
                if (sibling.spouses.contains(person2)) { 
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isSistersHusband(Person person1, Person person2) {
        for (Person sibling : person1.siblings) {
            if (sibling.gender.equals("female")) { // Check if sibling is female
                if (sibling.spouses.contains(person2)) { 
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isGreatGrandchild(Person person1, Person person2) {
        return isGreatGrandparent(person2, person1);
    }//done

    private boolean areSiblings(Person person1, Person person2) {
        if (person1.parents.isEmpty() || person2.parents.isEmpty()) {
            return false; 
        }

        for (Person parent1 : person1.parents) {
            if (person2.parents.contains(parent1)) {
                return true; 
            }
        }
        return false; 
    }//done

    private boolean isBrotherorSisterInLaw(Person person1, Person person2) {
        for (Person spouse : person1.spouses) {
            if (spouse.siblings.contains(person2)) {
                return true;
            }
        }
        for ( Person spouse : person2.spouses) {
            if (spouse.siblings.contains(person1)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isParentInLaw(Person person1, Person person2) {
        for (Person spouse : person2.spouses) {
            if (isParent(spouse,person1)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isChildInLaw(Person person1, Person person2) {
        for (Person spouse : person1.spouses) {
            if (spouse.parents.contains(person2)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isGrandchildInLaw(Person person1, Person person2) {
        for (Person parent : person2.children) {
                if ("female".equals(parent.gender) && isParent(person1,parent)) {
                    return true;
            }
        }
        return false;
    }//done

    private boolean isGreatGrandparentInLaw(Person person1, Person person2) {
        for (Person parent : person1.children) {
            if (isGreatGrandparentInLaw(parent,person2)) {
                return true;
        }
    }
        return false;
    }//done

    private boolean isNephewOrNiece(Person person1, Person person2) {
        for (Person parent : person1.parents) {
            if (isSiblingOf(parent, person2)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isSiblingOf(Person person1, Person person2) {
        return person1.siblings.contains(person2);
    }//done

    private boolean isGrandparent(Person grandparent, Person child) {
        for (Person parent : grandparent.children) {
            // Check if the parent is the father of the child
            if (isFather(parent, child)) {
                return true;
            }
            // for(Person chld : parent.children)
            // {
            //     if(chld.gender.equals("female") && isParent(chld,child))
            //     return true;
            // }
        }
        return false;
    }
    private boolean isFather(Person parent, Person child) {
        return parent.children.contains(child) && parent.gender.equals("male");
    }
    private boolean areBrotherOrSister(Person person1, Person person2) {
        return person1.parents.equals(person2.parents);
    }//done

    private boolean isSonOrDaughter(Person person1, Person person2) {
        return person1.parents.contains(person2);
    }//done

    private boolean isGrandfatherOrGrandmother(Person person1, Person person2) {
        return isGrandparent(person1, person2);
    }//done

    private boolean isGreatGrandfatherOrGreatGrandmother(Person person1, Person person2) {
        return isGreatGrandparent(person1, person2);
    }//done

    private boolean isGranduncleOrGrandaunt(Person person1, Person person2) {
        for (Person parent : person1.siblings) {
            if (isGrandparent(parent, person2)) {
                return true;
            }
        }
        return false;
    }//done

    private boolean isGreatGranduncleOrGreatGrandaunt(Person person1, Person person2) {
        for (Person parent : person1.siblings) {
                if (isGreatGrandparent(parent, person2)) {
                    return true;
            }
        }
        return false;
    }//done
}

public class Main {
    public static void main(String[] args) {
        FamilyTree familyTree = new FamilyTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add person");
            System.out.println("2. Find relation");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consuming the newline character left by nextInt()

            switch (option) {
                case 1:
                    System.out.print("Enter person's name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter person's gender (male/female): ");
                    String gender = scanner.nextLine();

                    System.out.print("Enter parent names (comma separated, leave blank if none): ");
                    String parentNames = scanner.nextLine();

                    System.out.print("Enter spouse names (comma separated, leave blank if none): ");
                    String spouseNames = scanner.nextLine();

                    System.out.print("Enter sibling names (comma separated, leave blank if none): ");
                    String siblingNames = scanner.nextLine();

                    System.out.print("Enter grandparent names (comma separated, leave blank if none): ");
                    String grandparentNames = scanner.nextLine();

                    System.out.print("Enter great-grandparent names (comma separated , leave blank if none): ");
                    String greatGrandparentNames = scanner.nextLine();

                    System.out.print("Enter uncle/aunt names (comma separated, leave blank if none): ");
                    String uncleAuntNames = scanner.nextLine();

                    System.out.print("Enter niece/nephew names (comma separated, leave blank if none): ");
                    String nieceNephewNames = scanner.nextLine();

                    // Creating lists from input, using empty lists if the input is blank
                    List<String> parentList = parentNames.isEmpty() ? Collections.emptyList() : Arrays.asList(parentNames.split(","));
                    List<String> spouseList = spouseNames.isEmpty() ? Collections.emptyList() : Arrays.asList(spouseNames.split(","));
                    List<String> siblingList = siblingNames.isEmpty() ? Collections.emptyList() : Arrays.asList(siblingNames.split(","));
                    List<String> grandparentList = grandparentNames.isEmpty() ? Collections.emptyList() : Arrays.asList(grandparentNames.split(","));
                    List<String> greatGrandparentList = greatGrandparentNames.isEmpty() ? Collections.emptyList() : Arrays.asList(greatGrandparentNames.split(","));
                    List<String> uncleAuntList = uncleAuntNames.isEmpty() ? Collections.emptyList() : Arrays.asList(uncleAuntNames.split(","));
                    List<String> nieceNephewList = nieceNephewNames.isEmpty() ? Collections.emptyList() : Arrays.asList(nieceNephewNames.split(","));

                    familyTree.addPerson(name, gender, parentList, spouseList, siblingList, grandparentList, greatGrandparentList, uncleAuntList, nieceNephewList);
                    break;
                case 2:
                    System.out.print("Enter person 1's name: ");
                    String name1 = scanner.nextLine();
                    System.out.print("Enter person 2's name: ");
                    String name2 = scanner.nextLine();

                    String relation = familyTree.findRelation(name1, name2);
                    if(relation!="One or both persons not found in the family tree.")
                    {
                    System.out.println(name1 + " is " + relation + " of " + name2);}
                    else
                    System.out.println("One or both persons not found in the family tree.");
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }
}