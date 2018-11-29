import pandas as pd
import xml.etree.ElementTree as ET


parsed = dict()


def iter_docs(target):
    return [parse_elem(child) for child in iter(target)]


def parse_elem(element):
    elem = dict()
    # elem["id"] = element.attrib["id"]
    for c in element:
        elem[c.tag] = c.text
        if c.tag == "region" or c.tag == "pollution":
            for cc in c:
                elem["{}_{}".format(c.tag, cc.tag)] = cc.text

    return elem


def get_consistencies(name):
    xml_file = open("../resources/data/{}.xml".format(name), "r")
    etree = ET.parse(xml_file)
    parsed = iter_docs(etree.getroot())
    doc_df = pd.DataFrame(parsed)
    with open("statistics_{}.txt".format(name), 'w') as f:
        print(doc_df.describe().to_string(), file=f)


if __name__ == '__main__':
    files = [
        "car_emissions_dupfree",
        "offers_dupfree",
        "region_emissions_target",
        "station_target",
        "vehicles_dupfree"
    ]
    for file in files:
        get_consistencies(file)
